package ru.otus.vseroev.dao;

import ru.otus.vseroev.domain.AnswerOption;
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
                        List<AnswerOption> options =
                                Arrays.stream(Arrays.copyOfRange(tokens, 1, tokens.length))
                                        .map(optionToken -> {
                                            String[] parts = optionToken.split(":");
                                            String text = parts[0];
                                            boolean correct = parts.length > 1 && Boolean.parseBoolean(parts[1]);
                                            return new AnswerOption(text, correct);
                                        })
                                        .collect(Collectors.toList());
                        return new Question(questionText, options);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // В случае ошибки выбрасываем RuntimeException
            throw new RuntimeException("Failed to read questions from resource: " + resourceName, e);
        }
    }
}
