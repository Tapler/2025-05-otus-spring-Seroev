package ru.otus.vseroev.service;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.dao.AuthorDao;
import ru.otus.vseroev.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public void insert(Author author) {
        authorDao.insert(author);
    }

    @Override
    public void update(Author author) {
        authorDao.update(author);
    }

    @Override
    public boolean deleteById(Long id) {
        return authorDao.deleteById(id);
    }
}
