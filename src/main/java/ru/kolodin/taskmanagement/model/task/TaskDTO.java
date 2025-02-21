package ru.kolodin.taskmanagement.model.task;

/**
 * ДТО задачи
 */
public class TaskDTO {

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
