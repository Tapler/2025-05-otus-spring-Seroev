package ru.otus.vseroev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestProcessImplTest {
    @Mock
    private IOService ioService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private TestProcessImpl testProcess;

    @BeforeEach
    void setUp() {
        // Устанавливаем локаль для теста
        LocaleContextHolder.setLocale(Locale.ENGLISH);
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
        // Мокаем сообщения, которые могут использоваться внутри теста
        given(messageSource.getMessage("answer.prompt", null, Locale.ENGLISH)).willReturn("Enter answer number:");
        given(messageSource.getMessage("valid.number", null, Locale.ENGLISH)).willReturn("Please enter a valid number!");
        given(messageSource.getMessage("positive.number", null, Locale.ENGLISH)).willReturn("Please enter a positive number!");

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
        verify(ioService, atLeastOnce()).println("Please enter a valid number!");
        verify(ioService, atLeastOnce()).println("Please enter a positive number!");
    }
}
