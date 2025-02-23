package ru.kolodin.taskmanagement.aspect.annotation.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация исключения для REST контроллера при недоступности ресурса,
 * при отсутствии данных и при неверных входных параметрах.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionNotFoundAndBadRequest {
}
