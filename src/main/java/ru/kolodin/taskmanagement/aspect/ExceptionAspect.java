package ru.kolodin.taskmanagement.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;

/**
 * Аспект исключений
 */
@Component
@Aspect
public class ExceptionAspect {

    /**
     * Проверка исключительных ситуаций в REST контроллере
     * @param joinPoint точка входа
     * @return результат работы метода
     */
    @Around("@annotation(ru.kolodin.taskmanagement.aspect.annotation.exception.ExceptionNotFoundAndBadRequest)")
    public Object restMethodExceptionNotFoundAndBadRequest(ProceedingJoinPoint joinPoint) {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

}
