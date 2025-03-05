package ru.kolodin.taskmanagement.aspect.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.kolodin.taskmanagement.kafka.KafkaTaskProducer;
import ru.kolodin.taskmanagement.kafka.MessageDeserializer;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("task.kafka.consumer.group-id")
    private String groupId;

    @Value("${task.kafka.bootstrap.server}")
    private String servers;

    @Value("${task.kafka.session.timeout.ms}")
    private String sessionTimeout;

    @Value("${task.kafka.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;

    @Value("${task.kafka.max.poll.records}")
    private String maxPollRecords;

    @Value("${task.kafka.max.poll.intervals.ms}")
    private String maxPollIntervalsMs;

    @Value("${task.kafka.topic.task-mng}")
    private String taskTopic;

    @Bean
    public ConsumerFactory<String, TaskIdStatusDto> consumerListenerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers); // server
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // consumer group
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Who will deserialize key
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class); // Who will deserialize value
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.kolodin.taskmanagement.model.task.TaskIdStatusDto"); // What to map into
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Allow mapping outside packages
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // Headers
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout); // Session timeout
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes); // Maximum message size
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords); // The number of messages read at a time and Commit of a single offset
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs); // The time allowed for the consumer to read the messages
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE); // Automatic confirmation of the offset by the consumer after processing the message
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start with an early message
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName()); // Custom error deserializer
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class); // Custom error deserializer

        DefaultKafkaConsumerFactory<String, TaskIdStatusDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TaskIdStatusDto> kafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") ConsumerFactory<String, TaskIdStatusDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TaskIdStatusDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory,
                                    ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners(((record, ex, deliveryAttempt) ->
                log.error(" RetryListener message = {}, offset = {} deliveryAttempt = {}",
                        ex.getMessage(), record.offset(), deliveryAttempt)));
        return  handler;
    }

    @Bean("taskTemplate")
    public KafkaTemplate<String, TaskIdStatusDto> kafkaTemplate(ProducerFactory<String, TaskIdStatusDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public KafkaTaskProducer producerTask(@Qualifier("taskTemplate") KafkaTemplate<String, TaskIdStatusDto> template) {
        template.setDefaultTopic(taskTopic);
        return new KafkaTaskProducer(template);
    }

    @Bean
    public ProducerFactory<String, TaskIdStatusDto> producerTaskFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
