package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Notification_tasks;

@Repository
public interface NotificationTaskRepository extends JpaRepository<Notification_tasks, Long> {

}
