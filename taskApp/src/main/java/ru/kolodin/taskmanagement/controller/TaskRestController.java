package ru.kolodin.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolodin.aspect.annotation.log.LogMethodCall;
import ru.kolodin.aspect.annotation.log.LogMethodException;
import ru.kolodin.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.service.db.TaskService;

import java.util.List;

/**
 * REST контроллер задач
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    /**
     * Создать новую задачу
     *
     * @param taskDto задача
     */
    @LogMethodCall(level = "WARN")
    @LogMethodException(level = "WARN")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody TaskDto taskDto) {
        taskService.add(taskDto);
    }

    /**
     * Получить задачу по ID
     *
     * @param id ID задачи
     * @return задача и статус ответа
     */
    @LogMethodCall(level = "WARN")
    @LogMethodException(level = "WARN")
    @LogMethodPerformance(level = "WARN")
    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable("id") Long id) {
        return taskService.getById(id);
    }

    /**
     * Обновить задачу
     *
     * @param id      ID задачи
     * @param taskDto ДТО задачи
     */
    @LogMethodCall(level = "INFO")
    @LogMethodException(level = "WARN")
    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id,
                       @RequestBody TaskDto taskDto) {
        taskService.update(id, taskDto);
    }

    /**
     * Удалить задачу по ID
     *
     * @param id ID задачи
     */
    @LogMethodCall(level = "WARN")
    @LogMethodException(level = "WARN")
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        taskService.deleteById(id);
    }

    /**
     * Получить список всех задач
     *
     * @return список задач и статус ответа
     */
    @LogMethodCall(level = "WARN")
    @LogMethodException(level = "WARN")
    @LogMethodPerformance(level = "WARN")
    @GetMapping("")
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }
}
