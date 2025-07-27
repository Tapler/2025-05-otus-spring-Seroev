package ru.otus.vseroev.service;

import ru.otus.vseroev.dao.AuthorDao;
import ru.otus.vseroev.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для авторов должен")
class AuthorServiceImplTest {
    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("возвращать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        List<Author> authors = List.of(new Author(1L, "A1"));
        when(authorDao.findAll()).thenReturn(authors);
        assertThat(authorService.findAll()).isEqualTo(authors);
        verify(authorDao, times(1)).findAll();
    }

    @DisplayName("возвращать автора по id")
    @Test
    void shouldReturnAuthorById() {
        Author author = new Author(2L, "A2");
        when(authorDao.findById(2L)).thenReturn(Optional.of(author));
        assertThat(authorService.findById(2L)).contains(author);
        verify(authorDao, times(1)).findById(2L);
    }

    @DisplayName("добавлять автора")
    @Test
    void shouldInsertAuthor() {
        Author author = new Author(null, "New");
        authorService.insert(author);
        verify(authorDao, times(1)).insert(author);
    }

    @DisplayName("обновлять автора")
    @Test
    void shouldUpdateAuthor() {
        Author author = new Author(3L, "Upd");
        authorService.update(author);
        verify(authorDao, times(1)).update(author);
    }

    @DisplayName("удалять автора по id и возвращать true")
    @Test
    void shouldDeleteAuthorById() {
        when(authorDao.deleteById(5L)).thenReturn(true);
        boolean deleted = authorService.deleteById(5L);
        assertThat(deleted).isTrue();
        verify(authorDao, times(1)).deleteById(5L);
    }

    @DisplayName("возвращать false при попытке удалить несуществующего автора")
    @Test
    void shouldReturnFalseIfAuthorNotFound() {
        when(authorDao.deleteById(999L)).thenReturn(false);
        boolean deleted = authorService.deleteById(999L);
        assertThat(deleted).isFalse();
        verify(authorDao, times(1)).deleteById(999L);
    }
}
