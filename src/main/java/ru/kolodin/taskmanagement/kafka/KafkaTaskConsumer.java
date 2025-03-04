package ru.kolodin.taskmanagement.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;
import ru.kolodin.taskmanagement.service.notification.NotificationService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskConsumer {

    private final NotificationService notificationService;

    @KafkaListener(id = "${task.kafka.consumer.group-id}",
                    topics = "${task.kafka.topic.task-mng}",
                     containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskIdStatusDto> taskIdStatusDtos,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Task consumer: Processing new messages...");
        try {
            taskIdStatusDtos.forEach(
                    taskIdStatusDto -> {
                        notificationService.toLog(taskIdStatusDto);
                        notificationService.toMail(taskIdStatusDto);
                    }
            );

        } finally {
            ack.acknowledge();
        }

        log.debug("Task consumer: Records have been processed.");
    }
}
