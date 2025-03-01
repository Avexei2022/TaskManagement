package ru.kolodin.taskmanagement.service.notification;

import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;

public interface NotificationService {
    void toLog(TaskIdStatusDto taskIdStatusDto);
    void toMail(TaskIdStatusDto taskIdStatusDto);
}
