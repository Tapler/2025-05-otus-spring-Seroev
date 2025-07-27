package ru.otus.vseroev.dao;

import ru.otus.vseroev.model.Author;
import ru.otus.vseroev.model.Book;
import ru.otus.vseroev.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DAO для книг должно")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {
    private static final Long EXISTING_BOOK_ID = 1L;
    @Autowired
    private BookDaoJdbc bookDao;
    @Autowired
    private GenreDaoJdbc genreDao;
    @Autowired
    private AuthorDaoJdbc authorDao;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @DisplayName("возвращать все книги из БД")
    @Test
    void shouldReturnAllBooks() {
        List<Book> books = bookDao.findAll();
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getAuthors()).isNotEmpty();
        assertThat(books.get(0).getGenre()).isNotNull();
    }

    @DisplayName("возвращать книгу по id")
    @Test
    void shouldReturnBookById() {
        Book book = bookDao.findAll().get(0);
        Optional<Book> found = bookDao.findById(book.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo(book.getTitle());
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        Genre genre = genreDao.findAll().get(0);
        Author author = authorDao.findAll().get(0);
        Book book = new Book(null, "TestBook", genre, List.of(author));
        bookDao.insert(book);
        List<Book> books = bookDao.findAll();
        assertThat(books.stream().anyMatch(b -> "TestBook".equals(b.getTitle()))).isTrue();
    }

    @DisplayName("обновлять книгу в БД")
    @Test
    void shouldUpdateBook() {
        Book book = bookDao.findAll().get(0);
        book.setTitle("UpdatedBook");
        bookDao.update(book);
        Optional<Book> updated = bookDao.findById(book.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("UpdatedBook");
    }

    @DisplayName("удалять заданную книгу по id")
    @Test
    void shouldDeleteBookById() {
        Book book = new Book(null, "ToDeleteBook", null, List.of());
        genreDao.insert(new Genre(null, "BookGenre"));
        Genre genre = genreDao.findAll().stream().filter(g -> "BookGenre".equals(g.getName())).findFirst().orElseThrow();
        book.setGenre(genre);
        bookDao.insert(book);
        Book inserted = bookDao.findAll().stream().filter(b -> "ToDeleteBook".equals(b.getTitle())).findFirst().orElseThrow();
        boolean deleted = bookDao.deleteById(inserted.getId());
        assertThat(deleted).isTrue();
        Optional<Book> deletedOpt = bookDao.findById(inserted.getId());
        assertThat(deletedOpt).isNotPresent();
    }

    @DisplayName("возвращать false при попытке удалить несуществующую книгу")
    @Test
    void shouldReturnFalseIfBookNotFound() {
        boolean deleted = bookDao.deleteById(999999L);
        assertThat(deleted).isFalse();
    }
}
