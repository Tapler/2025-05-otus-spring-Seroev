package ru.otus.vseroev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    private Genre genre;
    private List<Author> authors = new ArrayList<>(); // Инициализация для избежания NPE
}
