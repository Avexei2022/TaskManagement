package ru.kolodin.taskmanagement.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Тело/обертка исключения
 */
@Getter
@AllArgsConstructor
public class ExceptionBody {

    /**
     * Текст ссобщения
     */
    private String message;

    /**
     * Дата и время ошибки
     */
    private LocalDateTime dateTime;
}
