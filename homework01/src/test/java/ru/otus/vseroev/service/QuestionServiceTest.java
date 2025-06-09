package ru.otus.vseroev.service;

import org.junit.Before;
import org.junit.Test;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    private IOService ioService;
    private QuestionServiceImpl questionService;
    private List<Question> questions;

    @Before
    public void setUp() {
        ioService = mock(IOService.class);
        QuestionDao questionDao = mock(QuestionDao.class);
        AppProperties appProperties = mock(AppProperties.class);

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
        QuestionServiceImpl realService = new QuestionServiceImpl(questionDao, appProperties, ioService);
        questionService = spy(realService);
        doReturn(questions).when(questionService).getShuffledQuestions();
    }

    @Test
    public void printQuestions_shouldAskQuestionsAndCheckAnswers() {
        // Эмулируем ввод пользователя: фамилия, имя, ответы на вопросы
        when(ioService.readLine())
                .thenReturn("Ivanov") // last name
                .thenReturn("Ivan")   // first name
                .thenReturn("2")      // answer to Q1 (правильный)
                .thenReturn("1");     // answer to Q2 (правильный)

        questionService.printQuestions();

        // Проверяем, что вопросы и варианты были выведены
        verify(ioService).println(contains("Question 1"));
        verify(ioService).println(contains("Option 1"));
        verify(ioService).println(contains("Option 2"));
        verify(ioService).println(contains("Question 2"));
        verify(ioService).println(contains("Option A"));
        verify(ioService).println(contains("Option B"));

        // Проверяем, что результат теста выведен
        verify(ioService).println(contains("Test passed"));
    }

    @Test
    public void printQuestions_shouldHandleInvalidInput() {
        // Ввод: фамилия, имя, невалидный ответ, снова невалидный, затем валидный, затем валидный для второго вопроса
        when(ioService.readLine())
                .thenReturn("Petrov") // last name
                .thenReturn("Petr")   // first name
                .thenReturn("abc")    // invalid input (not a number)
                .thenReturn("0")      // invalid input (out of range)
                .thenReturn("2")      // valid answer for Q1
                .thenReturn("1");     // valid answer for Q2

        questionService.printQuestions();

        // Проверяем, что сообщения об ошибке были выведены
        verify(ioService, atLeastOnce()).println(contains("Please enter a valid number!"));
        verify(ioService, atLeastOnce()).println(contains("Please enter a positive number!"));
        // Проверяем, что вопросы и варианты были выведены
        verify(ioService).println(contains("Question 1"));
        verify(ioService).println(contains("Option 1"));
        verify(ioService).println(contains("Option 2"));
        verify(ioService).println(contains("Question 2"));
        verify(ioService).println(contains("Option A"));
        verify(ioService).println(contains("Option B"));
    }

    @Test
    public void printQuestions_shouldPassWithZeroPassCountAndOneQuestion() {
        // Настройки: один вопрос, проходной балл 0
        IOService io = mock(IOService.class);
        QuestionDao questionDao = mock(QuestionDao.class);
        AppProperties appProperties = mock(AppProperties.class);
        when(appProperties.getQuestionsCount()).thenReturn(1);
        when(appProperties.getPassCount()).thenReturn(0);
        List<AnswerOption> options = List.of(
                new AnswerOption("Only Option", true)
        );
        List<Question> edgeQuestions = new ArrayList<>(List.of(
                new Question("Test Question", options)
        ));
        when(questionDao.findAll()).thenReturn(edgeQuestions);
        QuestionServiceImpl service = new QuestionServiceImpl(questionDao, appProperties, io);

        when(io.readLine())
                .thenReturn("Sidorov") // last name
                .thenReturn("Sidr")    // first name
                .thenReturn("15");     // answer for the only question

        service.printQuestions();

        verify(io).println(contains("Test passed"));
    }
}
