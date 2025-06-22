package ru.otus.vseroev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.vseroev.config.QuestionSettings;
import ru.otus.vseroev.config.TestAppSettings;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @Mock
    private QuestionSettings questionSettings;
    @Mock
    private TestAppSettings testAppSettings;
    @Mock
    private TestProcess testProcess;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @BeforeEach
    void setUp() {
        given(messageSource.getMessage(eq("score.person"), any(), any(Locale.class)))
            .will(invocation -> {
                Object[] args = invocation.getArgument(1);
                return "Result for " + args[0];
            });
        given(messageSource.getMessage(eq("score.answers"), any(), any(Locale.class)))
            .will(invocation -> {
                Object[] args = invocation.getArgument(1);
                return "Correct answers: " + args[0] + " out of " + args[1];
            });
        given(messageSource.getMessage(eq("used.locale"), any(), any(Locale.class))).willReturn("");
        given(messageSource.getMessage(eq("result.success"), any(), any(Locale.class)))
            .willReturn("Test passed");
        given(messageSource.getMessage(eq("enter.lastName"), any(), any(Locale.class))).willReturn("");
        given(messageSource.getMessage(eq("enter.firstName"), any(), any(Locale.class))).willReturn("");
    }

    @Test
    void printQuestions_shouldPrintResultFromTestProcess() {
        List<AnswerOption> options1 = List.of(
                new AnswerOption("Option 1", false),
                new AnswerOption("Option 2", true)
        );
        List<AnswerOption> options2 = List.of(
                new AnswerOption("Option A", true),
                new AnswerOption("Option B", false)
        );
        List<Question> questions = new ArrayList<>(List.of(
                new Question("Question 1", options1),
                new Question("Question 2", options2)
        ));
        given(questionSettings.getQuestionsCount()).willReturn(2);
        given(questionSettings.getPassCount()).willReturn(1);
        given(testAppSettings.isShuffleQuestions()).willReturn(false);
        given(questionDao.findAll()).willReturn(questions);
        given(ioService.readLine()).willReturn("Ivanov", "Ivan");
        TestResult stubResult = new TestResult("Ivanov", "Ivan", 2, 2);
        given(testProcess.run(anyString(), anyString(), anyList())).willReturn(stubResult);

        questionService.printQuestions();

        verify(ioService).println(org.mockito.ArgumentMatchers.contains("Result for Ivanov Ivan"));
        verify(ioService).println(org.mockito.ArgumentMatchers.contains("Correct answers: 2 out of 2"));
        verify(ioService).println(org.mockito.ArgumentMatchers.contains("Test passed"));
    }
}
