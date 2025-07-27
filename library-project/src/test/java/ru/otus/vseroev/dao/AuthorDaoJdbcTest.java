package ru.otus.vseroev.dao;

import ru.otus.vseroev.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для авторов должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("возвращать всех авторов из БД")
    @Test
    void shouldReturnAllAuthors() {
        List<Author> authors = authorDao.findAll();
        assertThat(authors).isNotEmpty();
    }

    @DisplayName("возвращать автора по id")
    @Test
    void shouldReturnAuthorById() {
        Author author = authorDao.findAll().get(0);
        Optional<Author> found = authorDao.findById(author.getId());
        assertThat(found).isPresent().get().isEqualTo(author);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author author = new Author(null, "TestAuthor");
        authorDao.insert(author);
        List<Author> authors = authorDao.findAll();
        assertThat(authors.stream().anyMatch(a -> "TestAuthor".equals(a.getName()))).isTrue();
    }

    @DisplayName("обновлять автора в БД")
    @Test
    void shouldUpdateAuthor() {
        Author author = authorDao.findAll().get(0);
        author.setName("UpdatedAuthor");
        authorDao.update(author);
        Optional<Author> updated = authorDao.findById(author.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("UpdatedAuthor");
    }

    @DisplayName("удалять заданного автора по id")
    @Test
    void shouldDeleteAuthorById() {
        Author author = new Author(null, "ToDeleteAuthor");
        authorDao.insert(author);
        Author inserted = authorDao.findAll().stream()
                .filter(a -> "ToDeleteAuthor".equals(a.getName()))
                .findFirst().orElseThrow();
        boolean deleted = authorDao.deleteById(inserted.getId());
        assertThat(deleted).isTrue();
        Optional<Author> deletedOpt = authorDao.findById(inserted.getId());
        assertThat(deletedOpt).isNotPresent();
    }

    @DisplayName("возвращать false при попытке удалить несуществующего автора")
    @Test
    void shouldReturnFalseIfAuthorNotFound() {
        boolean deleted = authorDao.deleteById(999999L);
        assertThat(deleted).isFalse();
    }
}