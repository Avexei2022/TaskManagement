package ru.kolodin.taskmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ErrorResponse;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;


/**
 * Контроллер исключений
 */
@RestControllerAdvice
public class TaskRestExceptionHandler {

    /**
     * Ответ при недоступности ресурса или отсутствии данных
     *
     * @param e исключение
     * @return Ответ
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Ответ при неверном запросе
     *
     * @param e исключение
     * @return Ответ
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse mismatchException(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Ответ при в случае непредвиденных исключений
     *
     * @param e исключение
     * @return ответ
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorResponse applicationException(AppException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse anyOtherException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
