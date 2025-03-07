package ru.kolodin.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.kolodin.aspect.annotation.log.LogMethodCall;
import ru.kolodin.aspect.annotation.log.LogMethodException;
import ru.kolodin.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.aspect.annotation.log.LogMethodReturn;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Аспект логирования
 */
@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * Логирование вызова методов с входными параметрами
     * @param joinPoint вызов метода
     */
    @Before("@annotation(ru.kolodin.aspect.annotation.log.LogMethodCall)")
    public void logMethodCall(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Call method: ")
                .append(joinPoint.getSignature().getName())
                .append(" with args: ");
        Arrays.stream(joinPoint.getArgs()).forEach(
                arg -> stringBuilder.append(arg).append(", ")
        );
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogMethodCall logMethodCall = method.getAnnotation(LogMethodCall.class);
        Level level = Level.valueOf(logMethodCall.level());
        logger.atLevel(level).log(stringBuilder.toString());
    }

    /**
     * Логирование успешной работы метода с возвращаемыми значениями.
     * @param result возвращаемое значение.
     */
    @AfterReturning(
            value = "@annotation(ru.kolodin.aspect.annotation.log.LogMethodReturn)"
            , returning = "result")
    public void logMethodReturn(Object result, JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Method return class ")
                .append(result.getClass().getName())
                .append(" with value: ");
        if (result instanceof List<?>) {
            ((List<?>) result).forEach(obj -> stringBuilder.append(obj).append("\n"));
        } else {
            stringBuilder.append(result);
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogMethodReturn logMethodReturn = method.getAnnotation(LogMethodReturn.class);
        Level level = Level.valueOf(logMethodReturn.level());
        logger.atLevel(level).log(stringBuilder.toString());
    }

    /**
     * Логирование времени работы метода (производительности кода)
     * @param joinPoint метод
     * @return результат работы метода
     */
    @Around("@annotation(ru.kolodin.aspect.annotation.log.LogMethodPerformance)")
    public Object logMethodPerformance(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();

        Object proceeded;
        try {
            proceeded = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        String infoMessage = String.format(
                "Execution time of Method %s (ms): %s", joinPoint.getSignature(), endTime - startTime);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogMethodPerformance logMethodPerformance = method.getAnnotation(LogMethodPerformance.class);
        Level level = Level.valueOf(logMethodPerformance.level());
        logger.atLevel(level).log(infoMessage);
        return proceeded;
    }

    /**
     * Логирование исключений в методах
     * @param joinPoint метод
     * @param exception исключение
     */
    @AfterThrowing(
            pointcut = "@annotation(ru.kolodin.aspect.annotation.log.LogMethodException)",
                throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        String infoMessage = String.format("Exception caught in %s. Exception type is %s. The reason is %s",
                joinPoint.getSignature(),
                exception.getClass().getName(),
                exception.getMessage());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogMethodException logMethodException = method.getAnnotation(LogMethodException.class);
        Level level = Level.valueOf(logMethodException.level());
        logger.atLevel(level).log(infoMessage);
    }
}
