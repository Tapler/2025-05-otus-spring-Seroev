package ru.otus.vseroev.service;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.dao.GenreDao;
import ru.otus.vseroev.model.Genre;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreDao.findById(id);
    }

    @Override
    public void insert(Genre genre) {
        genreDao.insert(genre);
    }

    @Override
    public void update(Genre genre) {
        genreDao.update(genre);
    }

    @Override
    public boolean deleteById(Long id) {
        return genreDao.deleteById(id);
    }
}
