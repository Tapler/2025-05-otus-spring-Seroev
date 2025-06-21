package ru.otus.vseroev.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.vseroev.config.AppConfig;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDaoCsv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class QuestionServiceIntegrationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream testOut = new PrintStream(outContent);
    private final java.io.InputStream originalIn = System.in;

    @Autowired
    private AppProperties appProperties;

    @Before
    public void setUpStreams() {
        System.setOut(testOut);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void integrationTest_shouldPassTestFlow() {
        // Подготовка ввода пользователя (фамилия, имя, ответы)
        String userInput = "Ivanov\nIvan\n2\n1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inContent);

        QuestionDaoCsv questionDao = new QuestionDaoCsv(appProperties.getQuestionsFile());
        IOService ioService = new ConsoleIOService();
        TestProcess testProcess = new TestProcessImpl(ioService);
        QuestionServiceImpl service = new QuestionServiceImpl(questionDao, appProperties, ioService, testProcess);

        // Запуск теста
        service.printQuestions();

        String output = outContent.toString();
        assertTrue(output.contains("Test Question 1"));
        assertTrue(output.contains("Option 1"));
        assertTrue(output.contains("Option B"));
        assertTrue(output.contains("Test passed"));
    }
}
