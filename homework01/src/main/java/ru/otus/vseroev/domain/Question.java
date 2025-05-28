package ru.otus.vseroev.domain;

import java.util.List;

public class Question {
    private final String text;
    private final List<String> answerOptions;

    public Question(String text, List<String> answerOptions) {
        this.text = text;
        this.answerOptions = answerOptions;
    }

    public String getText() {
        return text;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }
}
