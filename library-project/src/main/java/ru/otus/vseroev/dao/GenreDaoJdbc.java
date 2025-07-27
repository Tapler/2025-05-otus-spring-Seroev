package ru.otus.vseroev.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.model.Genre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Genre> GENRE_ROW_MAPPER = (rs, rowNum) ->
            new Genre(rs.getLong("id"), rs.getString("name"));

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM genre", GENRE_ROW_MAPPER);
    }

    @Override
    public Optional<Genre> findById(Long id) {
        List<Genre> result = jdbcTemplate.query(
                "SELECT id, name FROM genre WHERE id = :id",
                Map.of("id", id),
                GENRE_ROW_MAPPER
        );
        return result.stream().findFirst();
    }

    @Override
    public void insert(Genre genre) {
        jdbcTemplate.update(
                "INSERT INTO genre (name) VALUES (:name)",
                Map.of("name", genre.getName())
        );
    }

    @Override
    public void update(Genre genre) {
        jdbcTemplate.update(
                "UPDATE genre SET name = :name WHERE id = :id",
                Map.of("id", genre.getId(), "name", genre.getName())
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int rows = jdbcTemplate.update("DELETE FROM genre WHERE id = :id", Map.of("id", id));
        return rows > 0;
    }
}
