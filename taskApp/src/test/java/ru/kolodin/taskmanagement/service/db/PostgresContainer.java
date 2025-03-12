package ru.kolodin.taskmanagement.service.db;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer {

    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
