package ru.kolodin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kolodin.aspect.LogAspect;
import ru.kolodin.aspect.LogAspectConfig;

import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogAutoConfiguration {

    private final LogProperties logProperties;

    public LogAutoConfiguration(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    @Bean
    @ConditionalOnProperty(name = "log.enable", havingValue = "true", matchIfMissing = true)
    public LogAspect logAspect(LogAspectConfig logConfig) {
        return new LogAspect(logConfig);
    }

    @Bean
    public LogAspectConfig logAspectConfig() {
        HashMap<String, String> levels = logProperties.getLogProperties();
       return new LogAspectConfig(levels);
    }
}
