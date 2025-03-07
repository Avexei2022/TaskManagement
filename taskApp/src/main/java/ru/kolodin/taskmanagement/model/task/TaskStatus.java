package ru.kolodin.taskmanagement.model.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Предопределенные значения статуса задачи
 */
@RequiredArgsConstructor
@Getter
public enum TaskStatus {

    WAITING("В ожидании"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершено");

    private final String value;
}
