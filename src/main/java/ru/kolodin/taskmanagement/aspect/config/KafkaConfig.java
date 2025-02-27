package ru.kolodin.taskmanagement.aspect.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.kolodin.taskmanagement.model.task.TaskDto;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("task-management")
    private String groupId;

    @Value("${task.kafka.bootstrap.server}")
    private String servers;

    @Value("${task.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;

    @Value("${task.kafka.max.partition.fetch.bytes:300000}")
    private String MaxPartitionFetchBytes;

    @Value("${task.kafka.max.poll.records:1}")
    private String maxPollRecords;

    @Value("${task.kafka.max.poll.intervals.ms:3000}")
    private String maxPollIntervalsMs;

    @Value("${task-management_client_registered}")
    private String clientTopic;


}
