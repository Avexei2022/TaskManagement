package ru.kolodin.taskmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ExceptionBody;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

/**
 * Контроллер исключений
 */
@RestControllerAdvice
public class TaskRestExceptionController {

    /**
     * Ответ при недоступности ресурса или отсутствии данных
     * @param e исключение
     * @return Ответ
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionBody> resourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionBody(e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Ответ при неверном запросе
     * @param e исключение
     * @return Ответ
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionBody> mismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionBody(e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Ответ при в случае непредвиденных исключений
     * @param e исключение
     * @return ответ
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionBody> applicationException(AppException e) {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .body(new ExceptionBody(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionBody> anyOtherException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .body(new ExceptionBody(e.getMessage(), LocalDateTime.now()));
    }
}
