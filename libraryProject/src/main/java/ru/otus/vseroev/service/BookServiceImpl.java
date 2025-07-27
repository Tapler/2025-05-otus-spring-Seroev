package ru.otus.vseroev.service;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.dao.BookDao;
import ru.otus.vseroev.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

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
        bookDao.insert(book);
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public boolean deleteById(Long id) {
        return bookDao.deleteById(id);
    }
}
