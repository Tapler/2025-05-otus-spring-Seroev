package ru.otus.vseroev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.vseroev.config.AppConfig;
import ru.otus.vseroev.service.QuestionService;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        QuestionService questionService = context.getBean(QuestionService.class);
        questionService.printQuestions();
    }
}
