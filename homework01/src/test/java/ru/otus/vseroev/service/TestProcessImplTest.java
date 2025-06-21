package ru.otus.vseroev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestProcessImplTest {
    @Mock
    private IOService ioService;
    @InjectMocks
    private TestProcessImpl testProcess;

    @BeforeEach
    void setUp() {
        // No need for setup in this test class
    }

    @Test
    void shouldReturnAllCorrectAnswers() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options),
                new Question("Q2", options)
        );
        given(ioService.readLine()).willReturn("2", "2");

        TestResult result = testProcess.run("Ivanov", "Ivan", questions);
        assertThat(result.getLastName()).isEqualTo("Ivanov");
        assertThat(result.getFirstName()).isEqualTo("Ivan");
        assertThat(result.getCorrectAnswers()).isEqualTo(2);
        assertThat(result.getTotalQuestions()).isEqualTo(2);
    }

    @Test
    void shouldReturnPartialCorrectAnswers() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options),
                new Question("Q2", options)
        );
        given(ioService.readLine()).willReturn("2", "1");

        TestResult result = testProcess.run("Petrov", "Petr", questions);
        assertThat(result.getCorrectAnswers()).isEqualTo(1);
        assertThat(result.getTotalQuestions()).isEqualTo(2);
    }

    @Test
    void shouldHandleInvalidInputAndAcceptValid() {
        List<AnswerOption> options = List.of(
                new AnswerOption("1", false),
                new AnswerOption("2", true)
        );
        List<Question> questions = List.of(
                new Question("Q1", options)
        );
        given(ioService.readLine()).willReturn("abc", "0", "2");

        TestResult result = testProcess.run("Sidorov", "Sidr", questions);
        assertThat(result.getCorrectAnswers()).isEqualTo(1);
        assertThat(result.getTotalQuestions()).isEqualTo(1);
        verify(ioService, atLeastOnce()).println(contains("Please enter a valid number!"));
        verify(ioService, atLeastOnce()).println(contains("Please enter a positive number!"));
    }
}
