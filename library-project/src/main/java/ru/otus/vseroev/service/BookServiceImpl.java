package ru.otus.vseroev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.vseroev.dao.BookDao;
import ru.otus.vseroev.exception.NotFoundException;
import ru.otus.vseroev.model.Author;
import ru.otus.vseroev.model.Book;
import ru.otus.vseroev.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final GenreService genreService;
    private final AuthorService authorService;

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public void insert(Book book) {
        checkGenreExists(book.getGenre());
        if (book.getAuthors() != null) {
            checkAuthorsExist(book.getAuthors());
        }
        bookDao.insert(book);
    }

    private void checkGenreExists(Genre genre) {
        if (genre == null || genreService.findById(genre.getId()).isEmpty()) {
            throw new NotFoundException("Жанр не найден");
        }
    }

    private void checkAuthorsExist(List<Author> authors) {
        List<Long> missingAuthors = authors.stream()
            .map(Author::getId)
            .filter(id -> authorService.findById(id).isEmpty())
            .collect(Collectors.toList());
        if (!missingAuthors.isEmpty()) {
            throw new NotFoundException("Автор(ы) с id " + missingAuthors + " не найден(ы)");
        }
    }

    @Override
    public void update(Book book) {
        Book existing = findById(book.getId()).orElseThrow(() -> new NotFoundException("Книга не найдена"));
        checkGenreExists(existing.getGenre());
        if (existing.getAuthors() != null) {
            checkAuthorsExist(existing.getAuthors());
        }
        // Обновляем только непустые поля
        if (book.getTitle() != null) {
            existing.setTitle(book.getTitle());
        }
        if (book.getGenre() != null) {
            existing.setGenre(book.getGenre());
        }
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            existing.setAuthors(book.getAuthors());
        }
        bookDao.update(existing);
    }

    @Override
    public boolean deleteById(Long id) {
        return bookDao.deleteById(id);
    }
}
