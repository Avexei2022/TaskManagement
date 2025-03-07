package ru.kolodin.taskmanagement.model.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ДТО задачи для Кафка
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TaskIdStatusDto {

    /**
     * Уникальный идентификатор задачи.
     */
    @JsonProperty("id")
    private Long id;

    /**
     * Статус задачи
     */
    @JsonProperty("status")
    private TaskStatus status;

}
