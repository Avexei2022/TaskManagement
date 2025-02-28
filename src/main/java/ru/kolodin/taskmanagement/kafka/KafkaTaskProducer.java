package ru.kolodin.taskmanagement.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskIdStatusDto> template;

    public void sendTo(String topic, TaskIdStatusDto taskIdStatusDto) {
        try {
            template.send(topic, taskIdStatusDto);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void send(TaskIdStatusDto taskIdStatusDto) {
        try {
            template.sendDefault(taskIdStatusDto);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
