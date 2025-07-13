package ru.otus.vseroev.shell;

import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.vseroev.service.QuestionService;

import java.util.Locale;

@ShellComponent
@RequiredArgsConstructor
public class QuizShellCommands {
    private final QuestionService questionService;

    @ShellMethod(value = "Start the quiz", key = {"start", "quiz"})
    public String startQuiz(@ShellOption(defaultValue = "en") String lang) {
        Locale locale = "ru".equalsIgnoreCase(lang) ? new Locale("ru", "RU") : Locale.ENGLISH;
        LocaleContextHolder.setLocale(locale);
        questionService.printQuestions();
        return new AttributedString("Quiz finished!", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toString();
    }
}
