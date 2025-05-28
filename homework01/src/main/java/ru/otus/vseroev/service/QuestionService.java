package ru.otus.vseroev.service;

import ru.otus.vseroev.dao.QuestionDao;

public class QuestionService {
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public void printQuestions() {
        // Получаем все вопросы и выводим их с вариантами ответов
        questionDao.findAll().forEach(question -> {
            System.out.println(question.getText());
            // Выводим варианты ответа, если они есть и не пустые
            question.getAnswerOptions().stream()
                    .filter(option -> !option.isBlank())
                    .forEach(option -> System.out.println("  - " + option));
            System.out.println();
        });
    }
}
