package ru.otus.vseroev.dao;

import ru.otus.vseroev.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
