package ru.otus.vseroev.service;

import ru.otus.vseroev.dao.BookDao;
import ru.otus.vseroev.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для книг должен")
class BookServiceImplTest {
    @Mock
    private BookDao bookDao;
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
        Book book = new Book(null, "New", null, List.of());
        bookService.insert(book);
        verify(bookDao, times(1)).insert(book);
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() {
        Book book = new Book(3L, "Upd", null, List.of());
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
}
