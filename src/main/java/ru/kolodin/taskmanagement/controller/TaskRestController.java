package ru.kolodin.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolodin.taskmanagement.aspect.annotation.exception.RestExceptionNotFoundAndBadRequest;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodCall;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodException;
import ru.kolodin.taskmanagement.aspect.annotation.log.LogMethodPerformance;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.Task;
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
    private final ModelMapper modelMapper;

    /**
     * Создать новую задачу
     * @param taskDto задача
     * @return статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @RestExceptionNotFoundAndBadRequest
    @PostMapping("")
    public ResponseEntity<Void> add(@RequestBody TaskDto taskDto) {
        taskDbService.add(modelMapper.map(taskDto, Task.class));
        return ResponseEntity.ok().build();
    }

    /**
     * Получить задачу по ID
     * @param id ID задачи
     * @return задача и статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @LogMethodPerformance
    @RestExceptionNotFoundAndBadRequest
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable("id") Long id) {
        TaskDto taskDto = modelMapper.map(taskDbService.getById(id), TaskDto.class);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param title заголовок задачи
     * @param description описание задачи
     * @return статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @RestExceptionNotFoundAndBadRequest
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id,
                                       @RequestParam String title,
                                       @RequestParam String description) {
        taskDbService.update(id, title, description);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить задачу по ID
     * @param id ID задачи
     * @return статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @RestExceptionNotFoundAndBadRequest
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskDbService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получить список всех задач
     * @return список задач и статус ответа
     */
    @LogMethodCall
    @LogMethodException
    @LogMethodPerformance
    @RestExceptionNotFoundAndBadRequest
    @GetMapping("")
    public ResponseEntity<List<TaskDto>> getAll() {
        List<TaskDto> tasksDto = taskDbService.getAll().stream().map(
                e -> modelMapper.map(e, TaskDto.class)
        ).toList();
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }
}
