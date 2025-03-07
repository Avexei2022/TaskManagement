package ru.kolodin.taskmanagement.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kolodin.aspect.annotation.log.LogMethodCall;
import ru.kolodin.aspect.annotation.log.LogMethodException;
import ru.kolodin.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.aspect.annotation.log.LogMethodReturn;
import ru.kolodin.taskmanagement.kafka.KafkaTaskProducer;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;
import ru.kolodin.taskmanagement.repository.TaskRepository;
import ru.kolodin.taskmanagement.util.TaskMapper;

import java.util.List;


/**
 * Сервис базы данных задач
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;

    @Value("${task.kafka.topic.task-mng}")
    private String topic;

    /**
     * Создать новую задачу и добавить в базу данных
     *
     * @param taskDto DTO задачи
     */
    @LogMethodCall(level = "LogMethodCall")
    @LogMethodException(level = "LogMethodException")
    @Override
    public void add(TaskDto taskDto) {
        taskRepository.save(TaskMapper.toNewEntity(taskDto));
    }

    /**
     * Получить задачу по ID
     *
     * @param id ID задачи
     * @return задача
     */
    @LogMethodCall(level = "LogMethodCall")
    @LogMethodReturn(level = "LogMethodReturn")
    @LogMethodException(level = "LogMethodException")
    @LogMethodPerformance(level = "LogMethodPerformance")
    @Override
    public TaskDto getById(Long id) {
        return TaskMapper.toDto(taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task with ID " + id + " not found")
        ));
    }

    /**
     * Обновить задачу
     *
     * @param id      ID задачи
     * @param taskDto ДТО задачи
     */
    @LogMethodCall(level = "LogMethodCall")
    @LogMethodException(level = "LogMethodException")
    @Override
    public void update(Long id, TaskDto taskDto) {
        if (taskRepository.existsById(id)) {
            try {
                Task taskToSave = TaskMapper.toUpdateEntityById(id, taskDto);
                Task taskToUpdate = taskRepository.findById(id).orElseThrow();
                boolean isAllSame = taskToSave.equals(taskToUpdate);
                boolean isStatusUpdated = !taskToSave.getStatus().equals(taskToUpdate.getStatus());
                if (isAllSame) {
                    throw new AppException("Nothing to update!");
                } else {
                    taskRepository.save(TaskMapper.toUpdateEntityById(id, taskDto));
                    if (isStatusUpdated) {
                        kafkaTaskProducer.sendTo(topic, new TaskIdStatusDto(id, taskDto.getStatus()));
                    }
                }
            } catch (AppException e) {
                throw new AppException("Unable to update task with ID " + id + ": " + e.getMessage());
            }
        } else {
            throw new ResourceNotFoundException("Task with ID " + id + " not found.");
        }
    }

    /**
     * Удалить задачу по ID
     *
     * @param id ID задачи
     */
    @LogMethodCall(level = "LogMethodCall")
    @LogMethodException(level = "LogMethodException")
    @Override
    public void deleteById(Long id) {
        if (taskRepository.existsById(id)) {
            try {
                taskRepository.deleteById(id);
            } catch (AppException e) {
                throw new AppException("Unable to delete task with ID " + id + ": " + e.getMessage());
            }

        } else {
            throw new ResourceNotFoundException(
                    "Task with ID " + id + " not found. Nothing to delete");
        }
    }

    /**
     * Получить список всех задач
     *
     * @return список задач
     */
    @LogMethodCall(level = "LogMethodCall")
    @LogMethodReturn(level = "LogMethodReturn")
    @LogMethodException(level = "LogMethodException")
    @LogMethodPerformance(level = "LogMethodPerformance")
    @Override
    public List<TaskDto> getAll() {
        List<TaskDto> taskDtos;
        try {
            taskDtos = taskRepository.findAll()
                    .stream().map(TaskMapper::toDto).toList();
        } catch (AppException e) {
            throw new AppException("Unable to get list of tasks. " + e.getMessage());
        }
        if (taskDtos.isEmpty()) {
            throw new ResourceNotFoundException("List of tasks is empty");
        }
        return taskDtos;
    }
}
