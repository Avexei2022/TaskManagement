package ru.kolodin.taskmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигуратор базовых настроек
 */
@Component
@Configuration
@EnableAspectJAutoProxy
@Getter
@Setter
public class BasicConfig {

    /**
     * Синхронный клиент REST
     * @return новый экземпляр
     */
    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

    /**
     * Структура данных, представляющая заголовки HTTP-запросов или ответов
     * @return новый экземпляр
     */
    @Bean
    public HttpHeaders headers() {
        return new HttpHeaders();
    }
}
