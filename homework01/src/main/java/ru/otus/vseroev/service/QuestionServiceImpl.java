package ru.otus.vseroev.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.vseroev.config.QuestionSettings;
import ru.otus.vseroev.config.TestAppSettings;
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
    private final QuestionSettings questionSettings;
    private final TestAppSettings testAppSettings;
    private final IOService ioService;
    private final TestProcess testProcess;
    private final MessageSource messageSource;

    @Override
    public void printQuestions() {
        // Вывод используемой локали
        String localeMsg = messageSource.getMessage("used.locale", new Object[]{LocaleContextHolder.getLocale()}, LocaleContextHolder.getLocale());
        ioService.println(localeMsg);
        ioService.print(messageSource.getMessage("enter.lastName", null, LocaleContextHolder.getLocale()));
        String lastName = ioService.readLine();
        ioService.print(messageSource.getMessage("enter.firstName", null, LocaleContextHolder.getLocale()));
        String firstName = ioService.readLine();

        List<Question> questions = testAppSettings.isShuffleQuestions()
                ? getShuffledQuestions()
                : questionDao.findAll().stream()
                .limit(questionSettings.getQuestionsCount())
                .collect(Collectors.toList());

        TestResult result = testProcess.run(lastName, firstName, questions);
        // Вывод результата с локализацией
        ioService.println("\n" + messageSource.getMessage("score.person", new Object[]{result.getLastName() + " " + result.getFirstName()}, LocaleContextHolder.getLocale()));
        ioService.println(messageSource.getMessage("score.answers", new Object[]{result.getCorrectAnswers(), result.getTotalQuestions()}, LocaleContextHolder.getLocale()));
        if (result.getCorrectAnswers() >= questionSettings.getPassCount()) {
            ioService.println(messageSource.getMessage("result.success", null, LocaleContextHolder.getLocale()));
        } else {
            ioService.println(messageSource.getMessage("result.fail", null, LocaleContextHolder.getLocale()));
        }
    }

    List<Question> getShuffledQuestions() {
        List<Question> all = questionDao.findAll();
        Collections.shuffle(all);
        return all.stream()
                .limit(questionSettings.getQuestionsCount())
                .collect(Collectors.toList());
    }
}
