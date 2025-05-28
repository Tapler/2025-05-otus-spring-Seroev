package ru.otus.vseroev;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.vseroev.service.QuestionService;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        QuestionService questionService = context.getBean(QuestionService.class);
        questionService.printQuestions();
    }
}
