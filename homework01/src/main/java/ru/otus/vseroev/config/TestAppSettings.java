package ru.otus.vseroev.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class TestAppSettings {
    private boolean shuffleQuestions;
    // поле для хранения локали по умолчанию (возможные значения ru, en)
    private String defaultLocale;
}
