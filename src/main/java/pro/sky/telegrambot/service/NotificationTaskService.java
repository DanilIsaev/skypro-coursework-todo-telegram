package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public SendMessage addTaskToTheToDoList(String messageText, Long chatId) {
        // Сообщение для пользователя
        SendMessage sendMessage;
        // Паттерн для работы с временем
        Pattern dateTimePattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.+)$");
        // Разбиение строки на дату и задачу
        Matcher matcher = dateTimePattern.matcher(messageText);

        if (matcher.matches()) {
            NotificationTask notificationTask = new NotificationTask();
            String responseDateTime = matcher.group(1);
            String responseMessage = matcher.group(2);

            //Преобразование строки в формат даты и время
            LocalDateTime localDateTime = LocalDateTime
                    .parse(responseDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            notificationTask.setChat_id(chatId);
            notificationTask.setNotification_time(localDateTime);
            notificationTask.setMessage(responseMessage);

            notificationTaskRepository.save(notificationTask);

            String response = "Задача создана";
            sendMessage = new SendMessage(chatId, response);
            return sendMessage;

        } else {
            String response = "Формат задачи введен неправильно, попробуйте: 01.01.2022 20:00 Сделать домашнюю работу";
            sendMessage = new SendMessage(chatId, response);

            return sendMessage;
        }
    }
}
