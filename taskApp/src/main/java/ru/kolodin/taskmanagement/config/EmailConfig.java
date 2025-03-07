package ru.kolodin.taskmanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Properties;

@Slf4j
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    private final EmailProperties emailProperties;

    public EmailConfig(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        HashMap<String, String> mailProperties = emailProperties.getMailProperties();
        log.warn("Проверка установки Почты {} {} {} {} {} {}",
                mailProperties.get("host"), mailProperties.get("port"), mailProperties.get("username"), mailProperties.get("password"),
                mailProperties.get("mail-from"), mailProperties.get("mail-to"));
        mailSender.setHost(mailProperties.get("host"));
        mailSender.setPort(Integer.parseInt(mailProperties.get("port")));
        mailSender.setUsername(mailProperties.get("username"));
        mailSender.setPassword(mailProperties.get("password"));

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        HashMap<String, String> mailProperties = emailProperties.getMailProperties();
        return new SimpleMailMessage();
    }

}
