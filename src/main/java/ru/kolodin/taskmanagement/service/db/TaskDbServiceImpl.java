package ru.kolodin.taskmanagement.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

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
    @Override
    public Task getById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.orElseThrow();
    }

    /**
     * Обновить задачу
     * @param task Задача
     */
    @Override
    public void update(Task task) {
        taskRepository.save(task);
    }

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Получить список всех задач
     * @return список задач
     */
    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
