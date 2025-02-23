package ru.kolodin.taskmanagement.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.aspect.annotation.exception.ExceptionNotFoundAndBadRequest;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodCall;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodException;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodReturn;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.repository.TaskRepository;

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
     * @param task задача
     */
    @LogMethodCall
    @LogMethodException
    @ExceptionNotFoundAndBadRequest
    @Override
    public void add(Task task) {
        task.setId(null);
        taskRepository.save(task);
    }

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return задача
     */
    @LogMethodCall
    @LogMethodReturn
    @LogMethodException
    @ExceptionNotFoundAndBadRequest
    @LogMethodPerformance
    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Элемент не найден или ресурс недоступен."));
    }

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param title заголовок задачи
     * @param description описание задачи
     */
    @LogMethodCall
    @LogMethodException
    @ExceptionNotFoundAndBadRequest
    @Override
    public void update(Long id, String title, String description) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Элемент не найден или ресурс недоступен."));
        task.setTitle(title);
        task.setDescription(description);
        taskRepository.save(task);
    }

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    @LogMethodCall
    @LogMethodException
    @ExceptionNotFoundAndBadRequest
    @Override
    public void deleteById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Элемент для удаления не найден или ресурс недоступен.");
        }
    }

    /**
     * Получить список всех задач
     * @return список задач
     */
    @LogMethodCall
    @LogMethodReturn
    @LogMethodException
    @ExceptionNotFoundAndBadRequest
    @LogMethodPerformance
    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
