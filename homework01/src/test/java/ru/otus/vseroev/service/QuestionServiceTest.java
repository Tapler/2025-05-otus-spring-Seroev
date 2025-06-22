package ru.otus.vseroev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @Mock
    private AppProperties appProperties;
    @Mock
    private TestProcess testProcess;
    @InjectMocks
    private QuestionServiceImpl questionService;

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
        given(appProperties.getQuestionsCount()).willReturn(2);
        given(appProperties.getPassCount()).willReturn(1);
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
