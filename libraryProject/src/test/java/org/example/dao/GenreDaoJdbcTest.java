package org.example.dao;

import org.example.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для жанров должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("возвращать все жанры из БД")
    @Test
    void shouldReturnAllGenres() {
        List<Genre> genres = genreDao.findAll();
        assertThat(genres).isNotEmpty();
    }

    @DisplayName("возвращать жанр по id")
    @Test
    void shouldReturnGenreById() {
        Genre genre = genreDao.findAll().get(0);
        Optional<Genre> found = genreDao.findById(genre.getId());
        assertThat(found).isPresent().get().isEqualTo(genre);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        Genre genre = new Genre(null, "TestGenre");
        genreDao.insert(genre);
        List<Genre> genres = genreDao.findAll();
        assertThat(genres.stream().anyMatch(g -> "TestGenre".equals(g.getName()))).isTrue();
    }

    @DisplayName("обновлять жанр в БД")
    @Test
    void shouldUpdateGenre() {
        Genre genre = genreDao.findAll().get(0);
        genre.setName("UpdatedName");
        genreDao.update(genre);
        Optional<Genre> updated = genreDao.findById(genre.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("UpdatedName");
    }

    @DisplayName("удалять заданный жанр по id")
    @Test
    void shouldDeleteGenreById() {
        Genre genre = new Genre(null, "ToDeleteGenre");
        genreDao.insert(genre);
        Genre inserted = genreDao.findAll().stream()
                .filter(g -> "ToDeleteGenre".equals(g.getName()))
                .findFirst().orElseThrow();
        boolean deleted = genreDao.deleteById(inserted.getId());
        assertThat(deleted).isTrue();
        Optional<Genre> deletedOpt = genreDao.findById(inserted.getId());
        assertThat(deletedOpt).isNotPresent();
    }

    @DisplayName("возвращать false при попытке удалить несуществующий жанр")
    @Test
    void shouldReturnFalseIfGenreNotFound() {
        boolean deleted = genreDao.deleteById(999999L);
        assertThat(deleted).isFalse();
    }
}
