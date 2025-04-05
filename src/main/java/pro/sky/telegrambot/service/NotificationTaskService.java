package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification_tasks;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class NotificationTaskService {
    Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    /*  Получение стартового сообщения */
    public SendMessage getResponseStartMessage(Long chatId) {
        logger.info("Отправка приветственного сообщения в chatId: {}", chatId);
        String response = "Добро пожаловать в учебный проект";

        return new SendMessage(chatId, response);
    }

    /* Добавление задачи в бд(Список дел)*/
    public SendMessage addTaskToTheToDoList(String messageText, Long chatId) {
        // Сообщение для пользователя
        SendMessage sendMessage;
        // Паттерн для работы с временем
        Pattern dateTimePattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.+)$");
        // Разбиение строки на дату и задачу
        Matcher matcher = dateTimePattern.matcher(messageText);

        if (matcher.matches()) {
            Notification_tasks notificationTasks = new Notification_tasks();
            String responseDateTime = matcher.group(1);
            String responseMessage = matcher.group(2);

            //Преобразование строки в формат даты и время
            LocalDateTime localDateTime = LocalDateTime
                    .parse(responseDateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            // Сохранение задачи в БД
            notificationTasks.setChat_id(chatId);
            notificationTasks.setNotification_time(localDateTime);
            notificationTasks.setMessage(responseMessage);
            notificationTaskRepository.save(notificationTasks);

            logger.info("Задача от чата {}, с наименование {}, создана на {}", chatId, responseMessage, localDateTime);

            // Возвращение сообщения пользователю
            String response = "Задача создана";
            sendMessage = new SendMessage(chatId, response);
            return sendMessage;

        } else {
            logger.info("Запрос переданный пользователем некорректен");

            String response = "Формат задачи введен неправильно, попробуйте: 01.01.2022 20:00 Сделать домашнюю работу";
            sendMessage = new SendMessage(chatId, response);

            return sendMessage;
        }
    }

    public List<SendMessage> sendingScheduledTask() {
        LocalDateTime localDateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Notification_tasks> notifications = notificationTaskRepository.findByNotification_time(localDateTimeNow);

        if (!notifications.isEmpty()) {
            return notifications.stream()
                    .map(task -> {
                        Long chatId = task.getChat_id();
                        String message = task.getMessage();
                        SendMessage sendMessage = new SendMessage(chatId, message);
                        return sendMessage;
                    })
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
