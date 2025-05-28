package ru.otus.vseroev.dao;

import ru.otus.vseroev.domain.Question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDaoCsv implements QuestionDao {
    private final String resourceName;

    public QuestionDaoCsv(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public List<Question> findAll() {
        // Открываем ресурс как поток ввода и оборачиваем в BufferedReader
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            // Читаем все строки файла, разбираем каждую строку в массив токенов,
            // фильтруем пустые строки, создаём объекты Question
            return reader.lines()
                    .map(line -> line.split(",")) // разбиваем строку по запятым
                    .filter(tokens -> tokens.length > 0) // пропускаем пустые строки
                    .map(tokens -> {
                        String questionText = tokens[0];
                        // Если есть варианты ответов, собираем их в список, иначе пустой список
                        List<String> options = tokens.length > 1
                                ? Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length))
                                : List.of();
                        return new Question(questionText, options);
                    })
                    .collect(Collectors.toList()); // собираем все вопросы в список
        } catch (Exception e) {
            // В случае ошибки выбрасываем RuntimeException
            throw new RuntimeException("Failed to read questions from resource: " + resourceName, e);
        }
    }
}
