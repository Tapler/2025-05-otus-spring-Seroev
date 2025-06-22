package ru.otus.vseroev.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties for questions configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "questions")
public class QuestionSettings {
    /**
     * English file with questions.
     */
    private String fileEn;

    /**
     * Russian file with questions.
     */
    private String fileRu;

    /**
     * Count of questions to pass.
     */
    private int passCount;

    /**
     * Total count of questions.
     */
    private int questionsCount;
}
