package ru.kolodin.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Основной класс приложения
 */
@SpringBootApplication
public class TaskManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApp.class, args);
    }
}
