package ru.kolodin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;


@ConfigurationProperties(prefix = "log.levels")
public class LogProperties {

    private HashMap<String, String> logProperties;

    public HashMap<String, String> getLogProperties() {
        return logProperties;
    }

    public void setLogProperties(HashMap<String, String> logProperties) {
        this.logProperties = logProperties;
    }
}
