package ru.kolodin.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kolodin.taskmanagement.model.exception.AppException;
import ru.kolodin.taskmanagement.model.exception.ResourceNotFoundException;
import ru.kolodin.taskmanagement.model.task.Task;
import ru.kolodin.taskmanagement.model.task.TaskDto;
import ru.kolodin.taskmanagement.model.task.TaskStatus;
import ru.kolodin.taskmanagement.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        saveTestTasks();
    }

    private void saveTestTasks() {
        if (taskRepository.count() < 1) {
            List<Task> tasks = new ArrayList<>();
            for (long i = 1L; i <= 7; i++) {
                Task task = new Task(null,
                        "Title - " + i,
                        "Description - " + i,
                        TaskStatus.WAITING,
                        1L);
                tasks.add(task);
            }
            taskRepository.saveAll(tasks);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Тест создания новой задачи")
    void add() throws Exception {
        TaskDto taskToAdd = new TaskDto(1L,
                "Title - 8",
                "Description - 8",
                TaskStatus.WAITING,
                1L);
        String taskJson = new ObjectMapper().writeValueAsString(taskToAdd);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("Тест Исключения при создания новой задачи в случае неверного запроса")
    void addBadRequestException() throws Exception {
        TaskDto taskToAdd = new TaskDto(1L,
                "Title - 9",
                "Description - 9",
                TaskStatus.WAITING,
                1L);
        String taskJson = new ObjectMapper().writeValueAsString(taskToAdd)
                .replace("WAITING", "UNDEFINED");
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Тест получения задачи по Id")
    void getById() throws Exception {
        mockMvc.perform(get("/tasks/1")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Title - 1")))
                .andExpect(jsonPath("$.description", is("Description - 1")))
                .andExpect(jsonPath("$.status", is("WAITING")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @Order(4)
    @DisplayName("Тест Исключения при получении задачи в случае ее отсутствия в БД")
    void getByIdNotFoundException() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        Assertions.assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()))
                .andExpect(result ->
                        Assertions.assertEquals( "Task with ID 10 not found",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(5)
    @DisplayName("Тест обновления задачи")
    void update() throws Exception {
        TaskDto taskUpdated = new TaskDto(2L,
                "Title - 2",
                "Description - 2",
                TaskStatus.IN_PROGRESS,
                1L);
        String taskJson = new ObjectMapper().writeValueAsString(taskUpdated);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @DisplayName("Тест Исключения при обновлении задачи в случае отсутствия задачи в БД")
    void updateResourceNotFoundException() throws Exception {
        TaskDto taskUpdated = new TaskDto(1L,
                "Title - 1",
                "Description - 1",
                TaskStatus.IN_PROGRESS,
                1L);
        String taskJson = new ObjectMapper().writeValueAsString(taskUpdated);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        Assertions.assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()))
                .andExpect(result ->
                        Assertions.assertEquals( "Task with ID 10 not found.",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(7)
    @DisplayName("Тест Исключения при обновлении задачи в случае отсутствия изменений")
    void updateNothingToUpdateException() throws Exception {
        TaskDto taskUpdated = new TaskDto(1L,
                "Title - 1",
                "Description - 1",
                TaskStatus.WAITING,
                1L);
        String taskJson = new ObjectMapper().writeValueAsString(taskUpdated);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNoContent())
                .andExpect(result ->
                        Assertions.assertInstanceOf(AppException.class, result.getResolvedException()))
                .andExpect(result ->
                        Assertions.assertEquals( "Unable to update task with ID 1: Nothing to update!",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(8)
    @DisplayName("Тест удаления задачи")
     void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/7")).andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @DisplayName("Тест Исключения при удалении задачи в случае её отсутствия в БД")
    void deleteResourceNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/10"))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        Assertions.assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()))
                .andExpect(result ->
                        Assertions.assertEquals( "Task with ID 10 not found. Nothing to delete",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(10)
    @DisplayName("Тест получения всего списка задач")
    void getAll() throws Exception {
        mockMvc.perform(get("/tasks/")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(11)
    @DisplayName("Тест Исключения при получении списка задач в случае пустого списка")
    void getAllResourceNotFoundException() throws Exception {
        taskRepository.deleteAll();
        mockMvc.perform(get("/tasks/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        Assertions.assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()))
                .andExpect(result ->
                        Assertions.assertEquals( "List of tasks is empty",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}