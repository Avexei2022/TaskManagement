package ru.kolodin.taskmanagement.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * Аспект логирования
 */
@Slf4j
@Component
@Aspect
public class LogAspect {

    /**
     * Логирование вызова методов с входными параметрами
     * @param joinPoint вызов метода
     */
    @Before("@annotation(ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodCall)")
    public void logMethodCall(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Call method: ")
                .append(joinPoint.getSignature().getName())
                .append(" with args: ");
        Arrays.stream(joinPoint.getArgs()).forEach(
                arg -> stringBuilder.append(arg).append(", ")
        );
        log.debug(stringBuilder.toString());
    }

    /**
     * Логирование успешной работы метода с возвращаемыми значениями.
     * @param result возвращаемое значение.
     */
    @AfterReturning(
            value = "@annotation(ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodReturn)"
            , returning = "result")
    public void logMethodReturn(Object result) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Method return class ")
                .append(result.getClass().getName())
                .append(" with value: ");
        if (result instanceof List<?>) {
            ((List<?>) result).forEach(obj -> stringBuilder.append(obj).append("\n"));
        } else {
            stringBuilder.append(result);
        }
        log.debug(stringBuilder.toString());
    }

    /**
     * Логирование времени работы метода (производительности кода)
     * @param joinPoint метод
     * @return результат работы метода
     */
    @Around("@annotation(ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodPerformance)")
    public Object logMethodPerformance(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();

        Object proceeded;
        try {
            proceeded = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        log.info("Execution time of Method {} (ms): {}",
                joinPoint.getSignature(),
                endTime - startTime);

        return proceeded;
    }

    /**
     * Логирование исключений в методах
     * @param joinPoint метод
     * @param exception исключение
     */
    @AfterThrowing(
            pointcut = "@annotation(ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodException)",
                throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception caught in {}. Exception type is {}",
                joinPoint.getSignature(),
                exception.getClass().getName());
    }
}
