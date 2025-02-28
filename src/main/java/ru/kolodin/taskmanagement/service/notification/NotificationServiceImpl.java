package ru.kolodin.taskmanagement.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kolodin.taskmanagement.model.task.TaskIdStatusDto;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    /**
     * @param taskIdStatusDto
     */
    @Override
    public void toLog(TaskIdStatusDto taskIdStatusDto) {
        log.info("Статус задачи с Id = {} обновлен на {}",
                taskIdStatusDto.getId(), taskIdStatusDto.getStatus().getValue());
    }

    /**
     * @param taskIdStatusDto
     */
    @Override
    public void toMail(TaskIdStatusDto taskIdStatusDto) {

    }
}
