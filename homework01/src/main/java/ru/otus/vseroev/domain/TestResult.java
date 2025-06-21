package ru.otus.vseroev.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestResult {
    private final String lastName;
    private final String firstName;
    private final int correctAnswers;
    private final int totalQuestions;
}