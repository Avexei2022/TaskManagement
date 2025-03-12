package ru.kolodin.taskmanagement.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.model.task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
class TaskMapperUnitTest {

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = createTask();
        taskDto = createTaskDto();
    }

    @Test
    @DisplayName("Тест маппинга ДТО в задачу")
    void toEntity() {
        Task taskActual = TaskMapper.toEntity(taskDto);

        assertEquals(task, taskActual);

    }

    @Test
    @DisplayName("Тест маппинга задачи в ДТО")
    void toDto() {

        TaskDto taskDtoActual = TaskMapper.toDto(task);

        assertEquals(taskDto, taskDtoActual);
    }

    @Test
    @DisplayName("Тест маппинга ДТО в новую задачу")
    void toNewEntity() {
        Task taskActual = TaskMapper.toNewEntity(taskDto);
        task.setId(null);
        assertEquals(task, taskActual);
    }

    @Test
    @DisplayName("Тест маппинга ДТО в задачу с заменой ID")
    void toUpdateEntityById() {
        Task taskActual = TaskMapper.toUpdateEntityById(2L, taskDto);
        task.setId(2L);
        assertEquals(task, taskActual);
    }

    private TaskDto createTaskDto() {
        return new TaskDto(1L, "title", "description", TaskStatus.WAITING, 1L);
    }

    private Task createTask() {
        return  new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
    }
}