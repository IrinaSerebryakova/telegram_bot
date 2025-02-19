package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.listener.TelegramApiException;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambot.TestConstants.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Spy
    private TelegramBot telegramBot;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SendResponse sendResponse = mock(SendResponse.class);

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void tryToSendMessageAndGetSuccessResult() {
        String chatIdStr = Long.toString(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, date.toString() + " " + text);

        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
    }

    @Test
    public void tryToSendMessageAndThrowTelegramApiException() {
        String chatIdStr = chatId.toString();
        SendMessage sendMessage = new SendMessage(chatIdStr, text);

        when(telegramBot.execute(sendMessage)).thenThrow(TelegramApiException.class);
        assertThrows(TelegramApiException.class, () ->
                telegramBot.execute(sendMessage));
    }

    @Test
    public void testSendNotificationsAndThrowException() throws Exception {
        when(notificationRepository.findNotificationByDateAndTime(date)).thenReturn(Collections.emptyList());
        when(telegramBot.execute(sendMessageWrong)).thenThrow(new TelegramApiException(exceptionMessage));

        assertThrows(TelegramApiException.class, () ->
                telegramBotUpdatesListener.sendScheduledNotifications(date));
        verify(notificationRepository, never()).deleteAll(anyList());
    }

    @Test
    public void sendScheduledNotifications() {
        LocalDateTime now = LocalDateTime.parse("2025-02-16T12:27");
        List<Notification> notificationsList = List.of(
                new Notification(614749807L, now, "Сделать домашнюю работу"));
            when(notificationRepository.findNotificationByDateAndTime(now)).thenReturn(notificationsList);
            telegramBotUpdatesListener.sendScheduledNotifications(now);
            notificationsList.forEach(notification -> {
                String expectedMessage = notification.getMessage();
                Long chatId = notification.getChatId();
            verify(notificationRepository).deleteAll(notificationsList);
            });
    }

    @Test
    void process() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat().id()).thenReturn(123L);
        when(message.chat().username()).thenReturn("testuser");

        telegramBotUpdatesListener.process(List.of(update));
        verify(telegramBot).execute(any(SendMessage.class));
    }
    @Test
    void startCommand() {
        Long chatId = 123L;
        String userName = "Test User";
        String expectedMessage = "Добро пожаловать, " + userName + "!";

        telegramBotUpdatesListener.startCommand(chatId, userName);
        verify(telegramBot).execute(argThat(sendMessage -> {
            String actualMessage = sendMessage.getParameters().get("text").toString();
            return actualMessage.equals(expectedMessage);
        }));
    }

    @Test
    void findNotificationByDateAndTime() {
        Long chatId = 1L;
        LocalDateTime now = LocalDateTime.parse("2025-02-16T12:27");
        when(telegramBotUpdatesListener.findNotificationByDateAndTime(now)).thenReturn(notificationList);
        notificationRepository.findNotificationByDateAndTime(now);
    }

    @Test
    void saveNotification_validInput() {
        Long chatId = 123L;
        String text = "15.03.2024 10:00 Test memo";

        telegramBotUpdatesListener.saveNotification(chatId, text);

        Pattern pattern = Pattern.compile("(\\s+)(\\d{2}.\\d{2}.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches()) {
            String date = matcher.group(2);
            String memo = matcher.group(4);
            LocalDateTime dateTime = LocalDateTime.parse(date + ":00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            verify(notificationRepository).save(new Notification(chatId, dateTime, memo));
        }
    }

    @Test
    void saveNotification_invalidInput() {
        Long chatId = 123L;
        String text = "Invalid input";

        telegramBotUpdatesListener.saveNotification(chatId, text);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void findAllNotifications() {
        Long chatId = 123L;
        List<Notification> notifications = List.of(
                new Notification(chatId, LocalDateTime.now(), "Memo 1"),
                new Notification(chatId, LocalDateTime.now().plusDays(1), "Memo 2")
        );
        when(notificationRepository.findAll()).thenReturn(notifications);
        telegramBotUpdatesListener.findAllNotifications(chatId);
        verify(telegramBot, times(2));
    }

    @Test
    void muteCommand() {
    }

    @Test
    void searchCommand() {
    }

    @Test
    void aboutCommand() {
    }

    @Test
    void settingsCommand() {
    }

 @Test
    void helpCommand() {
        String expectedMessage = notification.toString(); // Ожидаемое сообщение
        telegramBotUpdatesListener.helpCommand(chatId);

        verify(telegramBot).execute(argThat(sendMessage ->
                sendMessage.getParameters().get("text").toString().equals(expectedMessage)
        ));
    }

/*    @Test
    void stopCommand() {
        String expectedMessage = "Спасибо, что ты был(а) со мной, " + userName + "! До скорых встреч!";
        when(telegramBotUpdatesListener.stopCommand(any(Long.class))).thenReturn(expectedMessage);
    }*/
}
