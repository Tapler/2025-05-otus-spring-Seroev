# Library Management Application (Spring JDBC + Shell)

## Описание
Интерактивное CLI-приложение для управления библиотекой книг, авторов и жанров. Используется Spring Boot, Spring JDBC, H2 и Spring Shell.

## Как запустить
1. Соберите проект:
   ```sh
   mvn clean package
   ```
2. Запустите приложение:
   ```sh
   java -jar target/library-project-*.jar
   ```
3. После запуска появится интерактивная оболочка (Spring Shell).

## Основные возможности
- Управление книгами (добавление, просмотр, обновление, удаление)
- Управление авторами
- Управление жанрами
- Все операции доступны через интерактивные shell-команды

## Shell-команды с примерами

### Книги
- `books` или `list-books` — список всех книг
- `book --id 1` — показать книгу по id
- `add-book --title "Название" --genre-id 1 --author-ids "1,2"` — добавить книгу
- `update-book --id 1 --title "Новое название" --genre-id 2 --author-ids "2,3"` — обновить книгу
- `delete-book --id 1` — удалить книгу по id
- `add-author-to-book --book-id 1 --author-id 2` — добавить автора (id=2) к книге (id=1)
- `remove-author-from-book --book-id 1 --author-id 2` — удалить автора (id=2) из книги (id=1)

### Авторы
- `authors` или `list-authors` — список всех авторов
- `author --id 1` — показать автора по id
- `add-author --name "Имя Автора"` — добавить автора
- `update-author --id 1 --name "Новое имя"` — обновить автора
- `delete-author --id 1` — удалить автора по id

### Жанры
- `genres` или `list-genres` — список всех жанров
- `genre --id 1` — показать жанр по id
- `add-genre --name "Жанр"` — добавить жанр
- `update-genre --id 1 --name "Новый жанр"` — обновить жанр
- `delete-genre --id 1` — удалить жанр по id

## Описание объектной модели
- **Book**: id, title, genre (Genre), authors (List<Author>)
- **Author**: id, name
- **Genre**: id, name

### Связи
- Книга может иметь несколько авторов (many-to-many)
- Книга принадлежит одному жанру (many-to-one)

## Тестирование
- Покрытие unit-тестами DAO и сервисов (JUnit 5, Mockito)

## Требования
- Java 11+
- Maven

---

> Для справки по командам используйте `help` внутри shell.
