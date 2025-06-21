package ru.otus.vseroev.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.vseroev.config.AppProperties;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final AppProperties appProperties;
    private final IOService ioService;
    private final TestProcess testProcess;

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

        TestResult result = testProcess.run(lastName, firstName, questions);
        ioService.println("\nResult for " + result.getLastName() + " " + result.getFirstName() + ":");
        ioService.println("Correct answers: " + result.getCorrectAnswers() + " out of " + result.getTotalQuestions());
        if (result.getCorrectAnswers() >= appProperties.getPassCount()) {
            ioService.println("Test passed! Congratulations!");
        } else {
            ioService.println("Test not passed. Please try again.");
        }
    }

    List<Question> getShuffledQuestions() {
        List<Question> all = questionDao.findAll();
        Collections.shuffle(all);
        return all.stream()
                .limit(appProperties.getQuestionsCount())
                .collect(Collectors.toList());
    }
}
