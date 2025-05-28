package ru.otus.vseroev.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    // Поток для перехвата вывода в консоль
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // Сохраняем оригинальный System.out
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        // Перенаправляем System.out на наш поток перед каждым тестом
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        // Восстанавливаем System.out после теста
        System.setOut(originalOut);
    }

    @Test
    public void printQuestions_shouldPrintAllQuestionsAndOptions() {
        // Arrange
        QuestionDao questionDao = mock(QuestionDao.class);
        when(questionDao.findAll()).thenReturn(List.of(
                new Question("Question 1", List.of("Option 1", "Option 2")),
                new Question("Question 2", List.of("Option A"))
        ));
        QuestionService service = new QuestionService(questionDao);

        // Act
        service.printQuestions();

        // Assert: проверяем, что в выводе есть все нужные строки
        String output = outContent.toString();
        assertTrue(output.contains("Question 1"));
        assertTrue(output.contains("Option 1"));
        assertTrue(output.contains("Option 2"));
        assertTrue(output.contains("Question 2"));
        assertTrue(output.contains("Option A"));
    }
}
