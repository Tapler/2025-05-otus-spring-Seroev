package ru.otus.vseroev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Homework01Application {

    // CommandLineRunner больше не нужен, запуск теперь через Spring Shell

    public static void main(String[] args) {
        SpringApplication.run(Homework01Application.class, args);
    }

}
