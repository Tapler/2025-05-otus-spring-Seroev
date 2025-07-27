package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Author> AUTHOR_ROW_MAPPER = (rs, rowNum) ->
            new Author(rs.getLong("id"), rs.getString("name"));

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM author", AUTHOR_ROW_MAPPER);
    }

    @Override
    public Optional<Author> findById(Long id) {
        List<Author> result = jdbcTemplate.query(
                "SELECT id, name FROM author WHERE id = :id",
                Map.of("id", id),
                AUTHOR_ROW_MAPPER
        );
        return result.stream().findFirst();
    }

    @Override
    public void insert(Author author) {
        jdbcTemplate.update(
                "INSERT INTO author (name) VALUES (:name)",
                Map.of("name", author.getName())
        );
    }

    @Override
    public void update(Author author) {
        jdbcTemplate.update(
                "UPDATE author SET name = :name WHERE id = :id",
                Map.of("id", author.getId(), "name", author.getName())
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int rows = jdbcTemplate.update("DELETE FROM author WHERE id = :id", Map.of("id", id));
        return rows > 0;
    }
}
