package ru.otus.vseroev.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerOption {
    private final String text;
    private final boolean isCorrect;
}
