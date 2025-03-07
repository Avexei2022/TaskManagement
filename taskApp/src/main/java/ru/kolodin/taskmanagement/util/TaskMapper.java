package ru.kolodin.taskmanagement.util;

import org.springframework.stereotype.Component;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;

/**
 * Маппер задач
 */
@Component
public class TaskMapper {

    /**
     * ДТО в задачу
     *
     * @param taskDto ДТО задачи
     * @return задача
     */
    public static Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .userId(taskDto.getUserId())
                .build();
    }

    /**
     * Задача в ДТО
     *
     * @param task задача
     * @return ДТО задачи
     */
    public static TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userId(task.getUserId())
                .build();
    }

    /**
     * ДТО в новую задачу
     *
     * @param taskDto ДТО задачи
     * @return задача без Id
     */
    public static Task toNewEntity(TaskDto taskDto) {
        return Task.builder()
                .id(null)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .userId(taskDto.getUserId())
                .build();
    }

    /**
     * ДТО в новую задачу с заменой ID
     *
     * @param id      ID задачи для обновления
     * @param taskDto ДТО задачи
     * @return задача
     */
    public static Task toUpdateEntityById(Long id, TaskDto taskDto) {
        return Task.builder()
                .id(id)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .userId(taskDto.getUserId())
                .build();
    }

}
