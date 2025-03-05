package ru.kolodin.taskmanagement.model.task;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


/**
 * Задача
 */
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
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
     * Статус задачи
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    /**
     * Уникальный идентификатор пользователя
     */
    @Column(name = "user_id")
     private Long userId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(userId, task.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, userId);
    }
}
