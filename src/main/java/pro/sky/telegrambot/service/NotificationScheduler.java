package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationScheduler {

    private final NotificationRepository repository;
    private final TelegramBot telegramBot;

    public NotificationScheduler(NotificationRepository repository, TelegramBot telegramBot) {
        this.repository = repository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedDelay = 15000)
    public void sendNotifications() {
        List<Notification> notifications = repository.findAllByDateTimeBefore(LocalDateTime.now());

        notifications
                .forEach(notification -> {
                    telegramBot.execute(new SendMessage(notification.getChatId(), notification.getMessage()));
                    repository.delete(notification);
                });
    }
}
