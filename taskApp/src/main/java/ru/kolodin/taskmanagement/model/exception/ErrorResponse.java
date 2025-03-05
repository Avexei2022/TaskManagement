package ru.kolodin.taskmanagement.model.exception;

import lombok.Data;


/**
 * Ответ при исключении
 */
@Data
public class ErrorResponse {

    /**
     * Текст ссобщения
     */
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
