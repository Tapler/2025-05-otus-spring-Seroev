package ru.otus.vseroev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.vseroev.dao.BookDao;
import ru.otus.vseroev.exception.NotFoundException;
import ru.otus.vseroev.model.Author;
import ru.otus.vseroev.model.Book;
import ru.otus.vseroev.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для книг должен")
class BookServiceImplTest {
    @Mock
    private BookDao bookDao;
    @Mock
    private GenreService genreService;
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("возвращать все книги")
    @Test
    void shouldReturnAllBooks() {
        List<Book> books = List.of(new Book(1L, "Book1", null, List.of()));
        when(bookDao.findAll()).thenReturn(books);
        assertThat(bookService.findAll()).isEqualTo(books);
        verify(bookDao, times(1)).findAll();
    }

    @DisplayName("возвращать книгу по id")
    @Test
    void shouldReturnBookById() {
        Book book = new Book(2L, "Book2", null, List.of());
        when(bookDao.findById(2L)).thenReturn(Optional.of(book));
        assertThat(bookService.findById(2L)).contains(book);
        verify(bookDao, times(1)).findById(2L);
    }

    @DisplayName("добавлять книгу")
    @Test
    void shouldInsertBook() {
        Book book = new Book(null, "New", new Genre(1L, null), List.of());
        when(genreService.findById(1L)).thenReturn(Optional.of(new Genre(1L, "g")));
        bookService.insert(book);
        verify(bookDao, times(1)).insert(book);
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() {
        Book book = new Book(3L, "Upd", new Genre(1L, null), List.of());
        when(genreService.findById(1L)).thenReturn(Optional.of(new Genre(1L, "g")));
        bookService.update(book);
        verify(bookDao, times(1)).update(book);
    }

    @DisplayName("удалять книгу по id и возвращать true")
    @Test
    void shouldDeleteBookById() {
        when(bookDao.deleteById(5L)).thenReturn(true);
        boolean deleted = bookService.deleteById(5L);
        assertThat(deleted).isTrue();
        verify(bookDao, times(1)).deleteById(5L);
    }

    @DisplayName("возвращать false при попытке удалить несуществующую книгу")
    @Test
    void shouldReturnFalseIfBookNotFound() {
        when(bookDao.deleteById(999L)).thenReturn(false);
        boolean deleted = bookService.deleteById(999L);
        assertThat(deleted).isFalse();
        verify(bookDao, times(1)).deleteById(999L);
    }

    @DisplayName("выбрасывать NotFoundException если жанр не найден при insert")
    @Test
    void shouldThrowIfGenreNotFoundOnInsert() {
        Book book = new Book(null, "New", new Genre(99L, null), List.of());
        when(genreService.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.insert(book))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Жанр не найден");
    }

    @DisplayName("выбрасывать NotFoundException если автор не найден при insert")
    @Test
    void shouldThrowIfAuthorNotFoundOnInsert() {
        Book book = new Book(null, "New", new Genre(1L, null), List.of(new Author(10L, null)));
        when(genreService.findById(1L)).thenReturn(Optional.of(new Genre(1L, "g")));
        when(authorService.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.insert(book))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Автор(ы) с id [10] не найден(ы)");
    }

    @DisplayName("выбрасывать NotFoundException если жанр не найден при update")
    @Test
    void shouldThrowIfGenreNotFoundOnUpdate() {
        Book book = new Book(1L, "Upd", new Genre(99L, null), List.of());
        when(genreService.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.update(book))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Жанр не найден");
    }

    @DisplayName("выбрасывать NotFoundException если автор не найден при update")
    @Test
    void shouldThrowIfAuthorNotFoundOnUpdate() {
        Book book = new Book(1L, "Upd", new Genre(1L, null), List.of(new Author(10L, null)));
        when(genreService.findById(1L)).thenReturn(Optional.of(new Genre(1L, "g")));
        when(authorService.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.update(book))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Автор(ы) с id [10] не найден(ы)");
    }
}
