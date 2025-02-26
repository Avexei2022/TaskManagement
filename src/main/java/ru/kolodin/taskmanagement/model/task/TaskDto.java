package ru.kolodin.taskmanagement.model.task;

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
     * Уникальный идентификатор пользователя
     */
    private Long userId;
}
