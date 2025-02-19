package ru.kolodin.taskmanagement.model.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Задача
 */
@Entity
@Table(name = "tasks")
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class Task {

    /**
     * Уникальный идентификатор задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    /**
     * Заголовок задачи
     */
    @Column(name = "title", length = 64)
    private String title;

    /**
     * Описание задачи
     */
    @Column(name = "description")
    private String description;

    /**
     * Уникальный идентификатор пользователя
     */
    @Column(name = "user")
    private Long userId;
}
