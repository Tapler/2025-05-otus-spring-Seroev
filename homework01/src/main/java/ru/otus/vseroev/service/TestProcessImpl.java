package ru.otus.vseroev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestProcessImpl implements TestProcess {
    private final IOService ioService;

    /**
     * Проводит тестирование: задает вопросы, собирает ответы, возвращает результат.
     */
    @Override
    public TestResult run(String lastName, String firstName, List<Question> questions) {
        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (askAndCheckQuestion(questions.get(i), i)) {
                correctAnswers++;
            }
        }
        return new TestResult(lastName, firstName, correctAnswers, questions.size());
    }

    private boolean askAndCheckQuestion(Question question, int idx) {
        // Выводит текст вопроса и варианты ответа на консоль
        printQuestion(question, idx);
        int answerIndex = -1;
        boolean validInput = false;
        // Ввод повторяется, пока не будет введено положительное число.
        while (!validInput) {
            ioService.print("Your answer: ");
            String answer = ioService.readLine();
            try {
                answerIndex = Integer.parseInt(answer.trim()) - 1;
                if (answerIndex < 0) {
                    ioService.println("Please enter a positive number!");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                ioService.println("Please enter a valid number!");
            }
        }
        // Проверяет, что индекс в допустимых границах и выбранный вариант помечен как правильный
        return answerIndex < question.getAnswerOptions().size()
                && question.getAnswerOptions().get(answerIndex).isCorrect();
    }

    private void printQuestion(Question question, int idx) {
        // Печатаем номер и текст вопроса
        ioService.println((idx + 1) + ". " + question.getText());
        List<AnswerOption> opts = question.getAnswerOptions();
        // Перебираем и печатаем все варианты ответа с их номерами
        for (int j = 0; j < opts.size(); j++) {
            ioService.println("  " + (j + 1) + ". " + opts.get(j).getText());
        }
    }
}
