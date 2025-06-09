package ru.otus.vseroev.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {
    @Value("${questions.file}")
    private String questionsFile;

    @Value("${questions.pass-count}")
    private int passCount;

    @Value("${questions.count}")
    private int questionsCount;

    @Value("${app.shuffle-questions:true}")
    private boolean shuffleQuestions;
}
