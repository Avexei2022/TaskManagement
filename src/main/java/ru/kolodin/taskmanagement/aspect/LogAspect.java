package ru.kolodin.taskmanagement.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Аспект логирования
 */
@Component
@Aspect
public class LogAspect {

    private final Logger logger = Logger.getLogger(LogAspect.class.getName());

    /**
     * Логирование вызова методов с входными параметрами
     * @param joinPoint вызов метода
     */
    @Before("@annotation(ru.kolodin.taskmanagement.aspect.annotation.LogMethodCall)")
    public void logMethodCall(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Call method: ")
                .append(joinPoint.getSignature().getName())
                .append(" with args: ");
        Arrays.stream(joinPoint.getArgs()).forEach(
                arg -> stringBuilder.append(arg).append(", ")
        );
        logger.info(stringBuilder.toString());
    }

    @AfterReturning(
            value = "@annotation(ru.kolodin.taskmanagement.aspect.annotation.LogMethodReturn)"
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
        logger.info(stringBuilder.toString());
    }
}
