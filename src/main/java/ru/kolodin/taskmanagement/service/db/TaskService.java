package ru.kolodin.taskmanagement.service.db;

import ru.kolodin.taskmanagement.model.task.TaskDto;

import java.util.List;

/**
 * Интерфейс сервиса базы данных задач
 */
public interface TaskService {

    /**
     * Создать новую задачу и добавить в базу данных
     * @param taskDto DTO задачи
     */
    void add(TaskDto taskDto);

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return DTO задачи
     */
    TaskDto getById(Long id);

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param taskDto DTO задачи
     */
    void update(Long id, TaskDto taskDto);

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    void deleteById(Long id);

    /**
     * Получить список всех задач
     * @return список DTO задач
     */
    List<TaskDto> getAll();

}
