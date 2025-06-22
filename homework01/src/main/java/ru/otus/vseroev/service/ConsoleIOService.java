package ru.otus.vseroev.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ConsoleIOService implements IOService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void println(String s) {
        System.out.println(s);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}
