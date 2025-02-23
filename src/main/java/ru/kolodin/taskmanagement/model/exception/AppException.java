package ru.kolodin.taskmanagement.model.exception;

/**
 * Исключение
 */
public class AppException extends RuntimeException {

    /**
     * Общее исключение
     * @param message текст сообщения об ошибке
     */
    public AppException(String message) {
        super(message);
    }
}
