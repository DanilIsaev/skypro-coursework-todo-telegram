package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Notification_tasks;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<Notification_tasks, Long> {

    //@Query(value = "SELECT * FROM notification_tasks WHERE notification_time = :targetTime", nativeQuery = true)
    @Query("SELECT n FROM Notification_tasks n WHERE n.notification_time = :targetTime")
    List<Notification_tasks> findByNotification_time(@Param("targetTime") LocalDateTime localDateTime);
}
