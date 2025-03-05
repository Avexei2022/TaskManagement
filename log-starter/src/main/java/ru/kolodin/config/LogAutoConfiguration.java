package ru.kolodin.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import ru.kolodin.aspect.LogAspect;

@AutoConfiguration
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "log.enable", havingValue = "true", matchIfMissing = true)
    LogAspect logAspect() {
        return new LogAspect();
    }
}
