package ru.kolodin.taskmanagement.service.db;

import ru.kolodin.taskmanagement.model.task.Task;

import java.util.List;

/**
 * Интерфейс сервиса базы данных задач
 */
public interface TaskDbService {

    /**
     * Создать новую задачу и добавить в базу данных
     * @param task задача
     */
    void add(Task task);

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return задача
     */
    Task getById(Long id);

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param title заголовок задачи
     * @param description описание задачи
     */
    void update(Long id, String title, String description);

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    void deleteById(Long id);

    /**
     * Получить список всех задач
     * @return список задач
     */
    List<Task> getAll();

}
