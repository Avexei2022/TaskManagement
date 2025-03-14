package ru.kolodin.taskmanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest
class TaskManagementAppTest {

    @Test
    @DisplayName(value = "Контекст успешно инициализируется")
    void contextLoads() {
    }
}