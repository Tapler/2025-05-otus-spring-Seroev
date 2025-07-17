package ru.otus.vseroev.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.Question;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class QuestionServiceIntegrationTest {
    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionDao questionDao;
    @MockBean
    private MessageSource messageSource;
    @MockBean
    private IOService ioService;

    @Test
    void printQuestions_shouldPrintResultFromTestProcess() {
        // Arrange: подготовка данных
        List<Question> questions = List.of(new Question("Q1", List.of()), new Question("Q2", List.of()));
        given(questionDao.findAll()).willReturn(questions);
        given(messageSource.getMessage(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).willReturn("");
        given(ioService.readLine()).willAnswer(invocation -> "1");

        // Act
        questionService.printQuestions();

        // Assert
        verify(ioService, atLeast(1)).readLine();
    }
}
