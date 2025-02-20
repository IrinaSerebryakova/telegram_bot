package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.utils.CommandSupportUtils;

import java.util.List;

import static pro.sky.telegrambot.utils.CommandSupportUtils.chatId;

@Component
public class AllCommand implements TelegramCommand {
    private static final String ALL = "/all";
    private final NotificationRepository notificationRepository;

    public AllCommand(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public boolean support(Update update) {
        return CommandSupportUtils.isStringEqualsCommand(update, ALL);
    }

    @Override
    public SendMessage handle(Update update) {
        List<Notification> notifications = notificationRepository.findAll();
        StringBuilder all = new StringBuilder();
        if (!notifications.isEmpty()) {
            for (Notification notification : notifications) {
                all.append(notification.toString()).append("\n");
            }
            return new SendMessage(chatId(update), all.toString());
        }
        return new SendMessage(chatId(update), "Notifications not found.");
    }
}




