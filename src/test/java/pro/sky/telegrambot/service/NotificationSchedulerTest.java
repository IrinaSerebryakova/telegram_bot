package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.listener.TelegramApiException;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationSchedulerTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private NotificationScheduler notificationScheduler;

    public NotificationSchedulerTest() {

    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Long chatId = 123L;
    private final String wrongDateTimeString = "22.02.1985 20:57";
    private final LocalDateTime wrongDateTime = LocalDateTime.parse(wrongDateTimeString, formatter);
    private final String dateTimeString = "22.02.2025 20:57";
    private final LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
    private final String text = "Сделать домашнюю работу";
    private final Notification notification = new Notification(chatId, dateTime, text);
    private final List<Notification> notifications = List.of(notification);
    private final Notification wrongNotification = new Notification(chatId, wrongDateTime, text);
    private final List<Notification> wrongNotifications = List.of( wrongNotification);

    @Test
    void trySendNotificationsAndGetSuccessResult() throws TelegramApiException {
        when(repository.findAllByDateTimeBefore(any(LocalDateTime.class))).thenReturn(notifications);
        telegramBot.execute(any(SendMessage.class));
        notificationScheduler.sendNotifications();
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(repository, times(1)).delete(any(Notification.class));
    }

    @Test
    void trySendNotificationsWithoutAnyNotifications() {
        when(repository.findAllByDateTimeBefore(any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        notificationScheduler.sendNotifications();
        verify(repository, never()).delete(any(Notification.class));
        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    void trySendNotificationsAndThrowException() throws TelegramApiException {
        when(repository.findAllByDateTimeBefore(any(LocalDateTime.class))).thenReturn(wrongNotifications);
        when(telegramBot.execute(any(SendMessage.class)))
                .thenThrow(new TelegramApiException("Unexpected error"));
        notificationScheduler.sendNotifications();
        verify(repository, never()).delete(any(Notification.class));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
}