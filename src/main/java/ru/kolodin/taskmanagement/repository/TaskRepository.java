package ru.kolodin.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kolodin.taskmanagement.model.task.Task;

/**
 * Репозиторий задач
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
