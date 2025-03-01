package ru.kolodin.taskmanagement.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage simpleMailMessage;

    @Value("${mail.mail-from}")
    private String mailFrom;

    @Value("${mail.mail-to}")
    private String mailTo;

    @Override
    public void toLog(TaskIdStatusDto taskIdStatusDto) {
        log.info("Статус задачи с Id = {} обновлен на {}",
                taskIdStatusDto.getId(), taskIdStatusDto.getStatus().getValue());
    }

    @Override
    public void toMail(TaskIdStatusDto taskIdStatusDto) {
        simpleMailMessage.setFrom(mailFrom);
        simpleMailMessage.setTo(mailTo);
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
