package ru.kolodin.taskmanagement.service.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kolodin.taskmanagement.kafka.KafkaTaskProducer;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.model.task.TaskStatus;
import ru.kolodin.taskmanagement.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTaskProducer kafkaTaskProducer;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Тест создания новой задачи")
    public void add() {
        Task taskIn = new Task(null, "title", "description", TaskStatus.WAITING, 1L);
        Task taskOut = new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
        TaskDto taskDto = new TaskDto(1L, "title", "description", TaskStatus.WAITING, 1L);
        given(taskRepository.save(taskIn)).willReturn(taskOut);

        taskService.add(taskDto);

        verify(taskRepository).save(taskIn);
    }

    @Test
    @DisplayName("Тест получения задачи по ID")
    public void getById() {
        Task task = new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
        TaskDto taskDtoExpected = new TaskDto(1L,
                "title",
                "description",
                TaskStatus.WAITING,
                1L);
        given(taskRepository.findById(1L)).willReturn(Optional.of(task));

        TaskDto taskDtoActual = taskService.getById(1L);

        assertEquals(taskDtoExpected, taskDtoActual);
    }

    @Test
    @DisplayName("Тест исключения при получении задачи по ID")
    public void getByIdException() {
        given(taskRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> taskService.getById(1L));

        assertEquals( "Task with ID 1 not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест обновления задачи в БД без измененного статуса")
    public void updateTaskStatusIsSame() {
        given(taskRepository.existsById(1L)).willReturn(true);
        Task taskBeforeUpdate = new Task(1L, "title", "Old", TaskStatus.WAITING, 1L);
        Task taskAfterUpdate = new Task(1L, "title", "New", TaskStatus.WAITING, 1L);
        TaskDto taskDtoNew = new TaskDto(1L, "title", "New", TaskStatus.WAITING, 1L);
        given(taskRepository.findById(1L)).willReturn(Optional.of(taskBeforeUpdate));
        given(taskRepository.save(taskAfterUpdate)).willReturn(taskAfterUpdate);

        taskService.update(1L, taskDtoNew);

        verify(taskRepository).save(taskAfterUpdate);
    }

    @Test
    @DisplayName("Тест обновления задачи в БД с измененным статусом")
    public void updateTaskStatusIsNew() {
        given(taskRepository.existsById(1L)).willReturn(true);
        Task taskBeforeUpdate = new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
        Task taskAfterUpdate = new Task(1L, "title", "description", TaskStatus.IN_PROGRESS, 1L);
        TaskDto taskDtoNew = new TaskDto(1L, "title", "description", TaskStatus.IN_PROGRESS, 1L);
        given(taskRepository.findById(1L)).willReturn(Optional.of(taskBeforeUpdate));
        given(taskRepository.save(taskAfterUpdate)).willReturn(taskAfterUpdate);
        willDoNothing().given(kafkaTaskProducer).sendTo(any(), any());

        taskService.update(1L, taskDtoNew);

        verify(taskRepository).save(taskAfterUpdate);
    }

    @Test
    @DisplayName("Тест исключения во время обновления задачи при отсутствии задачи с Id")
    public void updateIsNotExistsException() {
        given(taskRepository.existsById(2L)).willReturn(false);
        TaskDto taskDtoNew = new TaskDto(1L, "title", "description", TaskStatus.WAITING, 1L);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> taskService.update(2L, taskDtoNew));

        assertEquals( "Task with ID 2 not found.", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест исключения во время обновления задачи при отсутствии изменений")
    public void updateTheSameException() {
        given(taskRepository.existsById(1L)).willReturn(true);
        Task taskBeforeUpdate = new Task(1L, "title", "description", TaskStatus.WAITING, 1L);
        TaskDto taskDtoNew = new TaskDto(1L, "title", "description", TaskStatus.WAITING, 1L);
        given(taskRepository.findById(1L)).willReturn(Optional.of(taskBeforeUpdate));

        AppException thrown = assertThrows(
                AppException.class, () -> taskService.update(1L, taskDtoNew));

        assertEquals( "Unable to update task with ID 1: Nothing to update!", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест удаления задачи по ID при наличии задачи в БД")
    public void deleteByIdIfExists() {
        given(taskRepository.existsById(1L)).willReturn(true);
        willDoNothing().given(taskRepository).deleteById(1L);

        taskService.deleteById(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Тест исключения во время удаления задачи по ID при отсутствии задачи в БД")
    public void deleteByIdIfNotExistsExpectedException() {
        given(taskRepository.existsById(1L)).willReturn(false);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> taskService.deleteById(1L));
        assertEquals( "Task with ID 1 not found. Nothing to delete", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест исключения во время удаления задачи по ID при наличии задачи в БД")
    public void deleteByIdExpectedException() {
        given(taskRepository.existsById(1L)).willReturn(true);
        doThrow(new AppException("Test Exception")).when(taskRepository).deleteById(1L);

        AppException thrown = assertThrows(
                AppException.class, () -> taskService.deleteById(1L));
        assertEquals( "Unable to delete task with ID 1: Test Exception", thrown.getMessage());
    }

    @Test
    @DisplayName("Тест получения списка задач из БД")
    public void getAll() {

        List<Task> tasks = createListOfTask(7L);
        List<TaskDto> taskDtos = createListOfTaskDto(7L);
        given(taskRepository.findAll()).willReturn(tasks);
        List<TaskDto> taskDtosActual = taskService.getAll();

        assertEquals(taskDtosActual, taskDtos);
    }

    private List<Task> createListOfTask(Long size) {
        List<Task> tasks = new ArrayList<>();
        for (Long i = 1L; i <= size; i++) {
            Task task = new Task(i,
                    "Title - " + i,
                    "Description - " + i,
                    TaskStatus.WAITING,
                    1L);
            tasks.add(task);
        }
        return tasks;
    }

    private List<TaskDto> createListOfTaskDto(Long size) {
        return createListOfTask(size).stream().map(task -> {
            return new TaskDto(task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getUserId());
        }).toList();
    }
}