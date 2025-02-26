package ru.kolodin.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodCall;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodException;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.service.db.TaskDbService;

import java.util.List;

/**
 * REST контроллер задач
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskDbService taskDbService;

    /**
     * Создать новую задачу
     * @param taskDto задача
     */
    @LogMethodCall
    @LogMethodException
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody TaskDto taskDto) {
        taskDbService.add(taskDto);
    }

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return задача и статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @LogMethodPerformance
    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable("id") Long id) {
        return taskDbService.getById(id);
    }

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param taskDto ДТО задачи
     */
    @LogMethodCall
    @LogMethodException
    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id,
                                       @RequestBody TaskDto taskDto) {
          taskDbService.update(id, taskDto);
    }

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     */
    @LogMethodCall
    @LogMethodException
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        taskDbService.deleteById(id);
    }

    /**
     * Получить список всех задач
     * @return список задач и статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @LogMethodPerformance
    @GetMapping("")
    public List<TaskDto> getAll() {
        return taskDbService.getAll();
    }
}
