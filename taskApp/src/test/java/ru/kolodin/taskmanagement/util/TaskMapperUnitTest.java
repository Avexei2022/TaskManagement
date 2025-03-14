package ru.kolodin.taskmanagement.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.model.task.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;


class TaskMapperUnitTest {

    @Test
    @DisplayName("Тест маппинга ДТО в задачу")
    void toEntity() {
        Task taskActual = TaskMapper.toEntity(createTaskDto());
        Task taskExpected = createTask();
        assertEquals(taskExpected, taskActual);

    }

    @Test
    @DisplayName("Тест маппинга задачи в ДТО")
    void toDto() {

        TaskDto taskDtoActual = TaskMapper.toDto(createTask());
        TaskDto taskDtoExpected = createTaskDto();
        assertEquals(taskDtoExpected, taskDtoActual);
    }

    @Test
    @DisplayName("Тест маппинга ДТО в новую задачу")
    void toNewEntity() {
        Task taskActual = TaskMapper.toNewEntity(createTaskDto());
        Task taskExpected = createTask();
        taskExpected.setId(null);
        assertEquals(taskExpected, taskActual);
    }

    @Test
    @DisplayName("Тест маппинга ДТО в задачу с заменой ID")
    void toUpdateEntityById() {
        Task taskActual = TaskMapper.toUpdateEntityById(2L, createTaskDto());
        Task taskExpected = createTask();
        taskExpected.setId(2L);
        assertEquals(taskExpected, taskActual);
    }

    private TaskDto createTaskDto() {
        return new TaskDto(1L, "title", "description", TaskStatus.WAITING, 1L);
    }

    private Task createTask() {
        return  new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
    }
}