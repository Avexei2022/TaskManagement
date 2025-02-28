package ru.kolodin.taskmanagement.model.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

/**
 * ДТО задачи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TaskDto {

    /**
     * Уникальный идентификатор задачи.
     */
    @JsonProperty("id")
    private Long id;

    /**
     * Заголовок задачи
     */
    @JsonProperty("title")
    private String title;

    /**
     * Описание задачи
     */
    @JsonProperty("description")
    private String description;

    /**
     * Статус задачи
     */
    @JsonProperty("status")
    private TaskStatus status;

    /**
     * Уникальный идентификатор пользователя
     */
    @JsonProperty("user_id")
    private Long userId;
}
