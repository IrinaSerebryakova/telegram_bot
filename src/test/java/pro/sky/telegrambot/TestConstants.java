package pro.sky.telegrambot;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

public class TestConstants {
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

}
