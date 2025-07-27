package org.example.shell;

import lombok.RequiredArgsConstructor;
import org.example.model.Genre;
import org.example.service.GenreService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {
    private final GenreService genreService;

    @ShellMethod(value = "Список всех жанров", key = {"genres", "list-genres"})
    public String listGenres() {
        List<Genre> genres = genreService.findAll();
        if (genres.isEmpty()) {
            return "Жанров не найдено.";
        }
        return genres.stream()
                .map(g -> String.format("[%d] %s", g.getId(), g.getName()))
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Показать жанр по id", key = {"genre", "get-genre"})
    public String getGenre(long id) {
        return genreService.findById(id)
                .map(g -> String.format("[%d] %s", g.getId(), g.getName()))
                .orElse("Жанр не найден");
    }

    @ShellMethod(value = "Добавить жанр", key = {"add-genre"})
    public String addGenre(String name) {
        Genre genre = new Genre(null, name);
        genreService.insert(genre);
        return "Жанр добавлен";
    }

    @ShellMethod(value = "Обновить жанр", key = {"update-genre"})
    public String updateGenre(long id, String name) {
        Genre genre = genreService.findById(id).orElse(null);
        if (genre == null) return "Жанр не найден";
        genre.setName(name);
        genreService.update(genre);
        return "Жанр обновлен";
    }

    @ShellMethod(value = "Удалить жанр по id", key = {"delete-genre"})
    public String deleteGenre(long id) {
        try {
            boolean deleted = genreService.deleteById(id);
            return deleted ? "Жанр удалён" : "Жанр с таким id не найден";
        } catch (DataIntegrityViolationException e) {
            return "Невозможно удалить жанр: к нему привязаны книги.";
        }
    }
}
