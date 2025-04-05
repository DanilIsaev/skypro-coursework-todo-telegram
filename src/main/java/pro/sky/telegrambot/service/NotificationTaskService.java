package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

@Service
public class NotificationTaskService {
    Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    private NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public SendMessage getResponseStartMessage(Long chatId) {
        logger.info("Отправка приветственного сообщения в chatId: {}", chatId);
        String response = "Добро пожаловать в учебный проект";
        SendMessage sendMessage = new SendMessage(chatId, response);
        return sendMessage;
    }
}
