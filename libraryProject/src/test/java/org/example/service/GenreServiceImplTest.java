package org.example.service;

import org.example.dao.GenreDao;
import org.example.model.Genre;
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

@DisplayName("Сервис для жанров должен")
class GenreServiceImplTest {
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("возвращать все жанры")
    @Test
    void shouldReturnAllGenres() {
        List<Genre> genres = List.of(new Genre(1L, "G1"));
        when(genreDao.findAll()).thenReturn(genres);
        assertThat(genreService.findAll()).isEqualTo(genres);
        verify(genreDao, times(1)).findAll();
    }

    @DisplayName("возвращать жанр по id")
    @Test
    void shouldReturnGenreById() {
        Genre genre = new Genre(2L, "G2");
        when(genreDao.findById(2L)).thenReturn(Optional.of(genre));
        assertThat(genreService.findById(2L)).contains(genre);
        verify(genreDao, times(1)).findById(2L);
    }

    @DisplayName("добавлять жанр")
    @Test
    void shouldInsertGenre() {
        Genre genre = new Genre(null, "New");
        genreService.insert(genre);
        verify(genreDao, times(1)).insert(genre);
    }

    @DisplayName("обновлять жанр")
    @Test
    void shouldUpdateGenre() {
        Genre genre = new Genre(3L, "Upd");
        genreService.update(genre);
        verify(genreDao, times(1)).update(genre);
    }

    @DisplayName("удалять жанр по id и возвращать true")
    @Test
    void shouldDeleteGenreById() {
        when(genreDao.deleteById(5L)).thenReturn(true);
        boolean deleted = genreService.deleteById(5L);
        assertThat(deleted).isTrue();
        verify(genreDao, times(1)).deleteById(5L);
    }

    @DisplayName("возвращать false при попытке удалить несуществующий жанр")
    @Test
    void shouldReturnFalseIfGenreNotFound() {
        when(genreDao.deleteById(999L)).thenReturn(false);
        boolean deleted = genreService.deleteById(999L);
        assertThat(deleted).isFalse();
        verify(genreDao, times(1)).deleteById(999L);
    }
}
