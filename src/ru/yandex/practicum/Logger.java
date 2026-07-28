package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private PrintWriter writer;

    // Конструктор для игры (лог в файл)
    public Logger() {
        try {
            this.writer = new PrintWriter(new FileWriter("log.txt", true), true);
        } catch (IOException e) {
            System.out.println("Критическая ошибка. Позовите разработчика: ");
            e.printStackTrace();
            this.writer = new PrintWriter(System.out, true);
        }
    }

    // Конструктор для тестов (лог в консоль)
    public Logger (PrintWriter writer) {
        this.writer = writer;
    }

    public void log(String message) {
        writer.println(message);
    }

    public void logError(Exception e) {
        e.printStackTrace(writer);
    }

    public void close() {
        writer.close();
    }
}
