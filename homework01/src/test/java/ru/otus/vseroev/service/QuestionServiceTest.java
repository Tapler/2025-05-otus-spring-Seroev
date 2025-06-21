package ru.otus.vseroev.service;

import org.junit.Before;
import org.junit.Test;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    private IOService ioService;
    private QuestionServiceImpl questionService;
    private List<Question> questions;
    private TestProcess testProcess;

    @Before
    public void setUp() {
        ioService = mock(IOService.class);
        QuestionDao questionDao = mock(QuestionDao.class);
        AppProperties appProperties = mock(AppProperties.class);
        testProcess = mock(TestProcess.class);

        when(appProperties.getQuestionsCount()).thenReturn(2);
        when(appProperties.getPassCount()).thenReturn(1);

        List<AnswerOption> options1 = List.of(
                new AnswerOption("Option 1", false),
                new AnswerOption("Option 2", true)
        );
        List<AnswerOption> options2 = List.of(
                new AnswerOption("Option A", true),
                new AnswerOption("Option B", false)
        );
        questions = new ArrayList<>(List.of(
                new Question("Question 1", options1),
                new Question("Question 2", options2)
        ));
        when(questionDao.findAll()).thenReturn(questions);
        // Создаем spy и мокируем getShuffledQuestions, чтобы отключить shuffle
        QuestionServiceImpl realService = new QuestionServiceImpl(questionDao, appProperties, ioService, testProcess);
        questionService = spy(realService);
        doReturn(questions).when(questionService).getShuffledQuestions();
    }

    @Test
    public void printQuestions_shouldPrintResultFromTestProcess() {
        // Эмулируем ввод пользователя: фамилия, имя, ответы на вопросы
        when(ioService.readLine())
                .thenReturn("Ivanov")
                .thenReturn("Ivan");
        TestResult stubResult = new TestResult("Ivanov", "Ivan", 2, 2);
        when(testProcess.run(anyString(), anyString(), anyList())).thenReturn(stubResult);

        // Act
        questionService.printQuestions();

        // Assert
        verify(ioService).println(contains("Result for Ivanov Ivan"));
        verify(ioService).println(contains("Correct answers: 2 out of 2"));
        verify(ioService).println(contains("Test passed"));
    }
}
