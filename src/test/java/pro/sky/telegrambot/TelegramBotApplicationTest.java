package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pro.sky.telegrambot.listener.TelegramApiException;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.util.Collections;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambot.TestConstants.*;


@SpringBootTest(classes = TelegramBotApplication.class)
class TelegramBotApplicationTest {

    @MockBean
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private SendResponse mockSendResponse;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    public void testSendNotificationsSuccessfully() throws Exception {
        when(notificationRepository.findNotificationByDateAndTime(date)).thenReturn(notificationList);
        when(telegramBot.execute(sendMessageSuccessfully)).thenReturn(mockSendResponse);

        telegramBotUpdatesListener.sendScheduledNotifications(date);
        verify(notificationRepository).deleteAll(notificationList);
    }

    @Test
    public void testSendNotificationsAndThrowException() throws Exception {
        when(notificationRepository.findNotificationByDateAndTime(date)).thenReturn(Collections.emptyList());
        when(telegramBot.execute(sendMessageWrong)).thenThrow(new TelegramApiException(exceptionMessage));

        Assertions
                .assertThrows(TelegramApiException.class, () ->
                telegramBotUpdatesListener.sendScheduledNotifications(date));
        verify(notificationRepository, never()).deleteAll(anyList());
    }
}
