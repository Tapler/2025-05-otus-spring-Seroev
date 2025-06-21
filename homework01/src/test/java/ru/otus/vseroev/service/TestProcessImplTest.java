package ru.otus.vseroev.service;

import org.junit.Before;
import org.junit.Test;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestProcessImplTest {
    private IOService ioService;
    private TestProcessImpl testProcess;

    @Before
    public void setUp() {
        ioService = mock(IOService.class);
        testProcess = new TestProcessImpl(ioService);
    }

    @Test
    public void shouldReturnAllCorrectAnswers() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options),
                new Question("Q2", options)
        );
        when(ioService.readLine()).thenReturn("2").thenReturn("2");

        TestResult result = testProcess.run("Ivanov", "Ivan", questions);
        assertEquals("Ivanov", result.getLastName());
        assertEquals("Ivan", result.getFirstName());
        assertEquals(2, result.getCorrectAnswers());
        assertEquals(2, result.getTotalQuestions());
    }

    @Test
    public void shouldReturnPartialCorrectAnswers() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options),
                new Question("Q2", options)
        );
        when(ioService.readLine()).thenReturn("2").thenReturn("1");

        TestResult result = testProcess.run("Petrov", "Petr", questions);
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(2, result.getTotalQuestions());
    }

    @Test
    public void shouldHandleInvalidInputAndAcceptValid() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options)
        );
        when(ioService.readLine()).thenReturn("abc").thenReturn("0").thenReturn("2");

        TestResult result = testProcess.run("Sidorov", "Sidr", questions);
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(1, result.getTotalQuestions());
        verify(ioService, atLeastOnce()).println(contains("Please enter a valid number!"));
        verify(ioService, atLeastOnce()).println(contains("Please enter a positive number!"));
    }
}
