package ru.otus.vseroev.shell;

import lombok.RequiredArgsConstructor;
import ru.otus.vseroev.exception.NotFoundException;
import ru.otus.vseroev.model.Author;
import ru.otus.vseroev.model.Book;
import ru.otus.vseroev.model.Genre;
import ru.otus.vseroev.service.AuthorService;
import ru.otus.vseroev.service.BookService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {
    private final BookService bookService;
    private final AuthorService authorService;

    @ShellMethod(value = "Список всех книг", key = {"books", "list-books"})
    public String listBooks() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            return "Книг не найдено.";
        }
        return books.stream()
                .map(b -> String.format("[%d] %s (жанр: %s, авторы: %s)",
                        b.getId(),
                        b.getTitle(),
                        b.getGenre() != null ? b.getGenre().getName() : "-",
                        b.getAuthors() != null && !b.getAuthors().isEmpty() ? b.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")) : "-"))
                .collect(Collectors.joining("\n"));
    }

    // Получить книгу по id
    @ShellMethod(value = "Показать книгу по id", key = {"book", "get-book"})
    public String getBook(long id) {
        return bookService.findById(id)
                .map(b -> String.format("[%d] %s (жанр: %s, авторы: %s)",
                        b.getId(),
                        b.getTitle(),
                        b.getGenre() != null ? b.getGenre().getName() : "-",
                        b.getAuthors() != null && !b.getAuthors().isEmpty() ? b.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")) : "-"))
                .orElse("Книга не найдена");
    }

    // Добавить книгу
    @ShellMethod(value = "Добавить книгу", key = {"add-book"})
    public String addBook(String title, Long genreId, String authorIds) {
        Book book = buildBook(null, title, genreId, authorIds);
        bookService.insert(book);
        return "Книга добавлена";
    }

    // Обновить книгу
    @ShellMethod(value = "Обновить книгу", key = {"update-book"})
    public String updateBook(
            @ShellOption long id,
            @ShellOption(defaultValue = ShellOption.NULL) String title,
            @ShellOption(defaultValue = ShellOption.NULL) Long genreId,
            @ShellOption(defaultValue = ShellOption.NULL) String authorIds) {
        Book book = buildBook(id, title, genreId, authorIds);
        try {
            bookService.update(book);
            return "Книга обновлена";
        } catch (NotFoundException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Ошибка обновления: " + e.getMessage();
        }
    }

    // метод для сборки Book из параметров
    private Book buildBook(Long id, String title, Long genreId, String authorIds) {
        Book book = new Book();
        if (id != null) book.setId(id);
        if (title != null) book.setTitle(title);
        if (genreId != null) book.setGenre(new Genre(genreId, null));
        if (authorIds != null && !authorIds.isBlank()) {
            List<Author> authors = java.util.Arrays.stream(authorIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .map(aid -> new Author(aid, null))
                    .collect(Collectors.toList());
            book.setAuthors(authors);
        }
        return book;
    }


    // Удалить книгу
    @ShellMethod(value = "Удалить книгу по id", key = {"delete-book"})
    public String deleteBook(long id) {
        boolean deleted = bookService.deleteById(id);
        return deleted ? "Книга удалена" : "Книга с таким id не найдена";
    }

    // Добавить автора к книге
    @ShellMethod(value = "Добавить автора к книге", key = {"add-author-to-book"})
    public String addAuthorToBook(@ShellOption long bookId, @ShellOption long authorId) {
        Optional<Book> bookOpt = bookService.findById(bookId);
        if (bookOpt.isEmpty()) {
            return "Книга с id " + bookId + " не найдена";
        }
        Optional<Author> authorOpt = authorService.findById(authorId);
        if (authorOpt.isEmpty()) {
            return "Автор с id " + authorId + " не найден";
        }
        Book book = bookOpt.get();
        if (book.getAuthors().stream().anyMatch(a -> a.getId().equals(authorId))) {
            return "Автор уже привязан к книге";
        }
        book.getAuthors().add(authorOpt.get());
        bookService.update(book);
        return "Автор добавлен к книге";
    }

    // Удалить автора из книги
    @ShellMethod(value = "Удалить автора из книги, должен быть минимум 1", key = {"remove-author-from-book"})
    public String removeAuthorFromBook(@ShellOption long bookId, @ShellOption long authorId) {
        Optional<Book> bookOpt = bookService.findById(bookId);
        if (bookOpt.isEmpty()) {
            return "Книга с id " + bookId + " не найдена";
        }
        Book book = bookOpt.get();
        boolean removed = book.getAuthors().removeIf(a -> a.getId().equals(authorId));
        if (!removed) {
            return "У книги нет автора с id " + authorId;
        }
        bookService.update(book);
        return "Автор удалён из книги";
    }
}
