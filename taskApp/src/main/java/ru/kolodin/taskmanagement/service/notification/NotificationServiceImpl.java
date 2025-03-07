package ru.kolodin.taskmanagement.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.config.EmailProperties;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;

import java.util.HashMap;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(EmailProperties.class)
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage simpleMailMessage;
    private final EmailProperties emailProperties;

    @Override
    public void toLog(TaskIdStatusDto taskIdStatusDto) {
        log.info("Статус задачи с Id = {} обновлен. Новый статус: {}",
                taskIdStatusDto.getId(), taskIdStatusDto.getStatus().getValue());
    }

    @Override
    public void toMail(TaskIdStatusDto taskIdStatusDto) {
        HashMap<String, String> mailProperties = emailProperties.getMailProperties();
        simpleMailMessage.setFrom(mailProperties.get("mail-from"));
        simpleMailMessage.setTo(mailProperties.get("mail-to"));
        simpleMailMessage.setSubject("The task info.");
        simpleMailMessage.setText("The task status has been changed. \n" +
                "Task ID = " + taskIdStatusDto.getId() + "\n" +
                "New status = " + taskIdStatusDto.getStatus());
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException ex) {
            log.debug(ex.getMessage());
        }
    }
}
