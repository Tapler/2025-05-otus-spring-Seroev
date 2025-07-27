package ru.otus.vseroev.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.model.Author;
import ru.otus.vseroev.model.Book;
import ru.otus.vseroev.model.Genre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    private static final RowMapper<Book> BOOK_ROW_MAPPER = (rs, rowNum) -> mapBook(rs);

    private static Book mapBook(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String title = rs.getString("title");
        Long genreId = rs.getLong("genre_id");
        Genre genre = genreId != 0 ? new Genre(genreId, null) : null;
        return new Book(id, title, genre, new ArrayList<>());
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = jdbcTemplate.query("SELECT id, title, genre_id FROM book", BOOK_ROW_MAPPER);
        loadGenresAndAuthors(books);
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        List<Book> books = jdbcTemplate.query(
                "SELECT id, title, genre_id FROM book WHERE id = :id",
                Map.of("id", id),
                BOOK_ROW_MAPPER
        );
        loadGenresAndAuthors(books);
        return books.stream().findFirst();
    }

    @Override
    public void insert(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", book.getTitle());
        params.put("genre_id", book.getGenre() != null ? book.getGenre().getId() : null);
        jdbcTemplate.update("INSERT INTO book (title, genre_id) VALUES (:title, :genre_id)", params);
        // Получить id только что вставленной книги (если нужно)
        // Добавить связи с авторами
        if (book.getAuthors() != null) {
            for (Author author : book.getAuthors()) {
                jdbcTemplate.update("INSERT INTO book_author (book_id, author_id) VALUES ((SELECT MAX(id) FROM book), :author_id)",
                        Map.of("author_id", author.getId()));
            }
        }
    }

    @Override
    public void update(Book book) {
        jdbcTemplate.update(
                "UPDATE book SET title = :title, genre_id = :genre_id WHERE id = :id",
                Map.of("id", book.getId(), "title", book.getTitle(), "genre_id", book.getGenre() != null ? book.getGenre().getId() : null)
        );
        // Обновить связи с авторами
        jdbcTemplate.update("DELETE FROM book_author WHERE book_id = :book_id", Map.of("book_id", book.getId()));
        if (book.getAuthors() != null) {
            for (Author author : book.getAuthors()) {
                jdbcTemplate.update("INSERT INTO book_author (book_id, author_id) VALUES (:book_id, :author_id)",
                        Map.of("book_id", book.getId(), "author_id", author.getId()));
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM book_author WHERE book_id = :book_id", Map.of("book_id", id));
        int rows = jdbcTemplate.update("DELETE FROM book WHERE id = :id", Map.of("id", id));
        return rows > 0;
    }

    private void loadGenresAndAuthors(List<Book> books) {
        if (books.isEmpty()) return;
        Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, b -> b));
        // Жанры
        for (Book book : books) {
            if (book.getGenre() != null && book.getGenre().getId() != null) {
                genreDao.findById(book.getGenre().getId()).ifPresent(book::setGenre);
            }
        }
        // Авторы
        List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
        Map<String, Object> params = Map.of("bookIds", bookIds);
        jdbcTemplate.query(
                "SELECT ba.book_id, a.id, a.name FROM book_author ba JOIN author a ON ba.author_id = a.id WHERE ba.book_id IN (:bookIds)",
                params,
                rs -> {
                    Long bookId = rs.getLong("book_id");
                    Author author = new Author(rs.getLong("id"), rs.getString("name"));
                    Book book = bookMap.get(bookId);
                    if (book != null) {
                        book.getAuthors().add(author);
                    }
                }
        );
    }
}
