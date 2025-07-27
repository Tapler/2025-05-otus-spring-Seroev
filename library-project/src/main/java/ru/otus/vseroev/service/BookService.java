package ru.otus.vseroev.service;

import ru.otus.vseroev.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    void insert(Book book);
    void update(Book book);
    /**
     * Удаляет книгу по id. Возвращает true, если была удалена, иначе false.
     */
    boolean deleteById(Long id);
}
