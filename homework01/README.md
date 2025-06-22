# Student Testing Application (Spring Boot)

## Задание
Приложение по проведению тестирования студентов (с самим тестированием)

### Требования
- Функционал тестирования студента
- Конфигурация в виде Java + Annotation-based конфигурации
- Файл настроек для приложения тестирования студентов
- Ввод-вывод на английском языке
- Пример юнит-теста для сервиса
- Пример интеграционного теста для сервиса

## Запуск приложения

### Через IDE
- Найдите класс `Homework01Application` (src/main/java/ru/otus/vseroev/Homework01Application.java)
- Запустите его как Java-приложение

### Через Maven
```sh
mvn spring-boot:run
```

### Через jar-файл
```sh
mvn clean package
java -jar target/student-testing-1.0-SNAPSHOT.jar
```

## Локализация
- Вопросы и все сообщения локализованы (en/ru)
