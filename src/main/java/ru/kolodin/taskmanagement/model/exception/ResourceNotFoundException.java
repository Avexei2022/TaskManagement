package ru.kolodin.taskmanagement.model.exception;

/**
 * Исключение
 */
public class ResourceNotFoundException extends AppException {

    /**
     * Исключение при недоступности ресурса или об отсутствии данных
     * @param message текст сообщения об ошибке
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
