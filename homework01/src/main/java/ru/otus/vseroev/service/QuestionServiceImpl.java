package ru.otus.vseroev.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.AnswerOption;
import ru.otus.vseroev.domain.Question;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final AppProperties appProperties;
    private final IOService ioService;

    @Override
    public void printQuestions() {
        ioService.print("Enter your last name: ");
        String lastName = ioService.readLine();
        ioService.print("Enter your first name: ");
        String firstName = ioService.readLine();

        List<Question> questions = appProperties.isShuffleQuestions()
                ? getShuffledQuestions()
                : questionDao.findAll().stream()
                .limit(appProperties.getQuestionsCount())
                .collect(Collectors.toList());
        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (askAndCheckQuestion(questions.get(i), i)) {
                correctAnswers++;
            }
        }
        ioService.println("\nResult for " + lastName + " " + firstName + ":");
        ioService.println("Correct answers: " + correctAnswers + " out of " + questions.size());
        if (correctAnswers >= appProperties.getPassCount()) {
            ioService.println("Test passed! Congratulations!");
        } else {
            ioService.println("Test not passed. Please try again.");
        }
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

    List<Question> getShuffledQuestions() {
        List<Question> all = questionDao.findAll();
        Collections.shuffle(all);
        return all.stream()
                .limit(appProperties.getQuestionsCount())
                .collect(Collectors.toList());
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
