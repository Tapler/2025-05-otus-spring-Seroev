package ru.otus.vseroev.dao;

import ru.otus.vseroev.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> findAll();
    Optional<Genre> findById(Long id);
    void insert(Genre genre);
    void update(Genre genre);
    /**
     * Удаляет жанр по id. Возвращает true, если был удалён, иначе false.
     */
    boolean deleteById(Long id);
}
