package org.example.shell;

import lombok.RequiredArgsConstructor;
import org.example.model.Author;
import org.example.service.AuthorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands {
    private final AuthorService authorService;

    @ShellMethod(value = "Список всех авторов", key = {"authors", "list-authors"})
    public String listAuthors() {
        List<Author> authors = authorService.findAll();
        if (authors.isEmpty()) {
            return "Авторов не найдено.";
        }
        return authors.stream()
                .map(a -> String.format("[%d] %s", a.getId(), a.getName()))
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Показать автора по id", key = {"author", "get-author"})
    public String getAuthor(long id) {
        return authorService.findById(id)
                .map(a -> String.format("[%d] %s", a.getId(), a.getName()))
                .orElse("Автор не найден");
    }

    @ShellMethod(value = "Добавить автора", key = {"add-author"})
    public String addAuthor(String name) {
        Author author = new Author(null, name);
        authorService.insert(author);
        return "Автор добавлен";
    }

    @ShellMethod(value = "Обновить автора", key = {"update-author"})
    public String updateAuthor(long id, String name) {
        Author author = authorService.findById(id).orElse(null);
        if (author == null) return "Автор не найден";
        author.setName(name);
        authorService.update(author);
        return "Автор обновлен";
    }

    @ShellMethod(value = "Удалить автора по id", key = {"delete-author"})
    public String deleteAuthor(long id) {
        try {
            boolean deleted = authorService.deleteById(id);
            return deleted ? "Автор удалён" : "Автор с таким id не найден";
        } catch (DataIntegrityViolationException e) {
            return "Невозможно удалить автора: к нему привязаны книги.";
        }
    }
}
