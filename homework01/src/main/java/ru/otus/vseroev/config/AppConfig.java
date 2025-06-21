package ru.otus.vseroev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.otus.vseroev.dao.QuestionDao;
import ru.otus.vseroev.dao.QuestionDaoCsv;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.otus.vseroev")
public class AppConfig {
    @Bean
    public QuestionDao questionDao(AppProperties appProperties) {
        return new QuestionDaoCsv(appProperties.getQuestionsFile());
    }
    // Этот бин необходим для поддержки аннотаций @Value
    // Позволяет Spring подставлять значения из application.properties
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}