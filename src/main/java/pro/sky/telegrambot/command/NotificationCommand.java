package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.listener.TelegramApiException;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambot.utils.CommandSupportUtils.chatId;
import static pro.sky.telegrambot.utils.CommandSupportUtils.text;

@Component
public class NotificationCommand implements TelegramCommand {

    private final Pattern regexPattern = Pattern.compile("(\\d{2}.\\d{2}.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private final NotificationRepository notificationRepository;

    public NotificationCommand(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public boolean support(Update update) {
        Optional<String> text = text(update);
        return text
                .map(it -> it.matches(regexPattern.pattern()))
                .orElse(false);
    }

    @Override
    public SendMessage handle(Update update) {
        Optional<String> text = text(update);
        if (text.isPresent()) {
            Matcher matcher = regexPattern.matcher(text.get());
            if(matcher.matches()) {
                String date = matcher.group(1);
                String memo = matcher.group(3);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                Notification notification = new Notification(
                        chatId(update),
                        LocalDateTime.parse(date, formatter),
                        memo);
                notificationRepository.save(notification);
            }
            return new SendMessage(chatId(update), "The Notification was successfully saved in the repository.");
        }
        return new SendMessage(chatId(update), "Unexpected error");
    }
}