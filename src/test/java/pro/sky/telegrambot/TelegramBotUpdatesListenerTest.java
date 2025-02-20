
package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    public static final Long chatId = 123L;
    public static final Long wrongChatId = -1L;
    public static final String userName = "TestUser";
    public static final String text = "Test message";
    public static final String exceptionMessage = "Ошибка при отправке сообщения: TelegramApiException";
    public static final LocalDateTime date = LocalDateTime.parse("16.02.2025 12:25");
    public static final Notification notification = new Notification(chatId, date, text);
    public static final List<Notification> notificationList = List.of(notification);
    public static final SendMessage sendMessageSuccessfully = new SendMessage(String.valueOf(chatId), date + " " + text);
    public static final SendMessage sendMessageWrong = new SendMessage(String.valueOf(wrongChatId), date + " " + text);

    @Spy
    private TelegramBot telegramBot;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SendResponse sendResponse = mock(SendResponse.class);

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

}
