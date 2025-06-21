package ru.otus.vseroev.service;

import ru.otus.vseroev.domain.Question;
import ru.otus.vseroev.domain.TestResult;

import java.util.List;

public interface TestProcess {
    TestResult run(String lastName, String firstName, List<Question> questions);
}
