package ru.kolodin.taskmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private HashMap<String, String> mailProperties;

    public HashMap<String, String> getMailProperties() {
        return mailProperties;
    }

    public void setMailProperties(HashMap<String, String> mailProperties) {
        this.mailProperties = mailProperties;
    }
}
