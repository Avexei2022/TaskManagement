package ru.kolodin.taskmanagement.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodCall;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodException;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodReturn;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.repository.TaskRepository;
import ru.kolodin.taskmanagement.util.TaskMapper;

import java.util.List;


/**
 * Сервис базы данных задач
 */
@Service
@RequiredArgsConstructor
public class TaskDbServiceImpl implements TaskDbService{

    private final TaskRepository taskRepository;

    /**
     * Создать новую задачу и добавить в базу данных
     * @param taskDto DTO задачи
     */
    @LogMethodCall
    @LogMethodException
    @Override
    public void add(TaskDto taskDto) {
        taskRepository.save(TaskMapper.toNewEntity(taskDto));
    }

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return задача
     */
    @LogMethodCall
    @LogMethodReturn
    @LogMethodException
    @LogMethodPerformance
    @Override
    public TaskDto getById(Long id) {
        return TaskMapper.toDto(taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task with ID " + id + " not found")
        ));
    }

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param taskDto ДТО задачи
     */
    @LogMethodCall
    @LogMethodException
    @Override
    public void update(Long id, TaskDto taskDto) {
        if (taskRepository.existsById(id)) {
            try {
                taskRepository.save(TaskMapper.toUpdateEntityById(id, taskDto));
            } catch (AppException e) {
                throw new AppException("Unable to update task with ID " + id);
            }
        } else {
            throw new ResourceNotFoundException("Task with ID " + id + " not found.");
        }
    }

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    @LogMethodCall
    @LogMethodException
    @Override
    public void deleteById(Long id) {
        if (taskRepository.existsById(id)) {
            try {
                taskRepository.deleteById(id);
            } catch (AppException e) {
                throw new AppException("Unable to delete task with ID " + id);
            }

        } else {
            throw new ResourceNotFoundException(
                    "Task with ID " + id + " not found. Nothing to delete");
        }
    }

    /**
     * Получить список всех задач
     * @return список задач
     */
    @LogMethodCall
    @LogMethodReturn
    @LogMethodException
    @LogMethodPerformance
    @Override
    public List<TaskDto> getAll() {
        List<TaskDto> taskDtos;
        try {
            taskDtos = taskRepository.findAll()
                    .stream().map(TaskMapper::toDto).toList();
        } catch (AppException e) {
            throw new AppException("Unable to get list of tasks.");
        }
        if (taskDtos.isEmpty()) {
            throw new ResourceNotFoundException("List of tasks is empty");
        }
        return taskDtos;
    }
}
