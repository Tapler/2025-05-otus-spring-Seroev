package ru.otus.vseroev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;

@SpringBootApplication
public class Homework01Application {

    @Bean
    public CommandLineRunner run(ru.otus.vseroev.service.QuestionService questionService) {
        return args -> {
            System.out.print("Choose language (en/ru, default: en): ");
            String lang = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();
            if (!"ru".equals(lang)) {
                lang = "en";
            }
            java.util.Locale locale = "ru".equals(lang) ? new java.util.Locale("ru", "RU") : java.util.Locale.ENGLISH;
            LocaleContextHolder.setLocale(locale);
            questionService.printQuestions();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Homework01Application.class, args);
    }

}
