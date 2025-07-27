package ru.otus.vseroev.service;

import ru.otus.vseroev.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();
    Optional<Author> findById(Long id);
    void insert(Author author);
    void update(Author author);
    /**
     * Удаляет автора по id. Возвращает true, если был удалён, иначе false.
     */
    boolean deleteById(Long id);
}
