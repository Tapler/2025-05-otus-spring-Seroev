package ru.otus.vseroev.shell;

import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.vseroev.config.TestAppSettings;
import ru.otus.vseroev.service.QuestionService;

import java.util.Locale;

@RequiredArgsConstructor
@ShellComponent
public class QuizShellCommands {
    private final QuestionService questionService;
    private final TestAppSettings testAppSettings;
    private final MessageSource messageSource;

    @ShellMethod(value = "Start the quiz", key = {"start", "quiz"})
    public String startQuiz(@ShellOption(defaultValue = "") String lang) {
        // Если параметр lang не передан, используем локаль из настроек приложения
        String localeStr = lang.isEmpty() ? testAppSettings.getDefaultLocale() : lang;
        Locale locale = "ru".equalsIgnoreCase(localeStr) ? new Locale("ru", "RU") : Locale.ENGLISH;
        LocaleContextHolder.setLocale(locale);
        questionService.printQuestions();
        // Локализованный вывод окончания викторины
        String finishedMsg = messageSource.getMessage("quiz.finished", null, locale);
        return new AttributedString(finishedMsg, AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toString();
    }
}
