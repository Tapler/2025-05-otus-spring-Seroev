package ru.otus.vseroev.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDaoCsv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ru.otus.vseroev.config.AppConfig.class)
@TestPropertySource("classpath:application-test.properties")
class QuestionServiceIntegrationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream testOut = new PrintStream(outContent);
    private final java.io.InputStream originalIn = System.in;

    @Autowired
    private AppProperties appProperties;

    @BeforeEach
    void setUpStreams() {
        System.setOut(testOut);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void integrationTest_shouldPassTestFlow() {
        String userInput = "Ivanov\nIvan\n2\n1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inContent);

        QuestionDaoCsv questionDao = new QuestionDaoCsv(appProperties.getQuestionsFile());
        IOService ioService = new ConsoleIOService();
        TestProcess testProcess = new TestProcessImpl(ioService);
        QuestionServiceImpl service = new QuestionServiceImpl(questionDao, appProperties, ioService, testProcess);

        service.printQuestions();

        String output = outContent.toString();
        assertThat(output)
            .contains("Test Question 1")
            .contains("Option 1")
            .contains("Option B")
            .contains("Test passed");
    }
}
