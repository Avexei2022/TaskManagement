package ru.kolodin.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolodin.taskmanagement.model.task.Task;
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
     * @param task задача
     * @return статус ответа
     */
    @PostMapping("")
    public ResponseEntity<Void> add(@RequestBody Task task) {
        taskDbService.add(task);
        return ResponseEntity.ok().build();
    }

    /**
     * Получит задачу по ID
     * @param id ID задачи
     * @return задача и статус ответа
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") Long id) {
        Task task = taskDbService.getById(id);
        return new ResponseEntity<>(task,HttpStatus.OK);
    }

    /**
     * Обновить задачу
     * @param id ID задачи
     * @param title заголовок задачи
     * @param description описание задачи
     * @return статус ответа
     */
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
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskDbService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получить список всех задач
     * @return список задач и статус ответа
     */
    @GetMapping("")
    public ResponseEntity<List<Task>> getAll() {
        List<Task> tasks = taskDbService.getAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
