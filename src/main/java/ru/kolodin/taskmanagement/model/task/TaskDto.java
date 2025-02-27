package ru.kolodin.taskmanagement.model.task;

import jakarta.persistence.Column;
import lombok.*;

/**
 * ДТО задачи
 */
@Data
@Builder
public class TaskDto {

    /**
     * Уникальный идентификатор задачи.
     */
    private Long id;

    /**
     * Заголовок задачи
     */
    private String title;

    /**
     * Описание задачи
     */
    private String description;

    /**
     * Статус задачи
     */
    private TaskStatus status;

    /**
     * Уникальный идентификатор пользователя
     */
    private Long userId;
}
