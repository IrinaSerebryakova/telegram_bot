package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String START = "/start";
    private static final String MEMO = "/memo";
    private static final String ALL = "/all";
    private static final String NOW = "/now";
    private static final String HELP = "/help";
    private static final String SETTINGS = "/settings";
    private static final String ABOUT = "/about";
    private static final String SEARCH = "/search";
    private static final String MUTE = "/mute";
    private static final String STOP = "/stop";

    private static final Long chatId = 614749807L;

    private static final Map<String, String> notifications = Map.of(
            START, " начать общение с ботом\n",
            MEMO, " создать напоминание\n",
            ALL, " увидеть список напоминаний\n",
            NOW, " какие напоминания установлены на время \"сейчас\"\n",
            SETTINGS, " изменить настройки\n",
            SEARCH, " поиск информации по ключевому слову\n",
            MUTE, " отключить уведомления\n",
            STOP, " закончить общение с ботом\n",
            HELP, " показать список команд\n",
            ABOUT, " показать информацию о боте\n"
    );

    @Autowired
    private TelegramBot telegramBot;

    private Notification notification;
    private SendResponse sendResponse;
    private NotificationRepository notificationRepository;

    @Autowired
    public TelegramBotUpdatesListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public TelegramBotUpdatesListener() {
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        logger.info("The method 'process' was called");
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String message = update.message().text();
            String dateTime = String.valueOf(update.message().date());
            Long chatId = update.message().chat().id();

            switch (message) {
                case START -> {
                    String userName = update.message().chat().username();
                    startCommand(chatId, userName);
                }
                case MEMO -> {
                    saveNotification(chatId, message);
                }
                case ALL -> {
                    findAllNotifications(chatId);
                }
                case NOW -> {
                    findNotificationByDateAndTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                }
                case HELP -> helpCommand(chatId);
                case SETTINGS -> settingsCommand();
                case ABOUT -> aboutCommand();
                case SEARCH -> searchCommand();
                case MUTE -> muteCommand();
                case STOP -> {
                    String userName = update.message().chat().username();
                    stopCommand(chatId, userName);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendScheduledNotifications(LocalDateTime dateTime) {
        List<Notification> notificationsList = findNotificationByDateAndTime(dateTime); // Передаем dateTime
        notificationsList.forEach(notification ->
                sendMessage(notification.getChatId(), notification.getMessage()));
        notificationRepository.deleteAll(notificationsList);
    }

    public List<Notification> findNotificationByDateAndTime(LocalDateTime dateTime) {
        logger.info("The method 'findNotificationByDateAndTime' was called with dateTime: {}", dateTime);
        return notificationRepository.findNotificationByDateAndTime(dateTime);
    }

    public void sendMessage(Long chatId, String text) {
        logger.info("The method 'sendMessage' was called");
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            logger.info("Send response: {}", sendResponse);
        } catch (TelegramApiException e) {
            logger.error("In process of sending of the message was happened something wrong with it, and more exactly, it was ", e);
            logger.debug("Check you app on debug, because throws ", e);
        }
    }

    public void startCommand(Long chatId, String userName) {
        logger.info("The method 'startCommand' was called");
        String text = "Добро пожаловать, " + userName + "!";
        sendMessage(chatId, text);
    }

    public void saveNotification(Long chatId, String text) {
        logger.info("The method 'saveNotification' was called");
        if (text.startsWith("/memo")) {
            String textWithoutCommand = text.substring("/memo".length()).trim();
            Pattern pattern = Pattern.compile("(\\d{2}.\\d{2}.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
            Matcher matcher = pattern.matcher(textWithoutCommand);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            if (matcher.matches()) {
                String date = matcher.group(2);
                String memo = matcher.group(4);
                LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                Notification note = new Notification(chatId, dateTime, memo);
                notificationRepository.save(note);
                logger.info("The Notification was successfully saved in the repository.");
                String confirmationMessage = "Напоминание сохранено на " +
                        dateTime.format(formatter) + ": " + memo;
                sendMessage(chatId, confirmationMessage);
            }
        } else {
            sendMessage(chatId, "Для создания напоминания используйте команду /memo, " +
                    "а затем дату и текст в формате: 'ДД.ММ.ГГГГ ЧЧ:ММ текст'");
        }
    }

    public void findAllNotifications(Long chatId) {
        logger.info("The method 'findAllNotifications' was called");
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification memo : notifications) {
            sendMessage(chatId, String.valueOf(memo));
        }
    }

    public void muteCommand() {
        logger.info("The method 'muteCommand' was called");

    }

    public void searchCommand() {
        logger.info("The method 'searchCommand' was called");

    }

    public void aboutCommand() {
        logger.info("The method 'aboutCommand' was called");

    }

    public void settingsCommand() {
        logger.info("The method 'settingsCommand' was called");

    }

    public void helpCommand(Long chatId) {
        logger.info("The method 'helpCommand' was called");
        sendMessage(chatId, notifications.toString());
    }

    public void stopCommand(Long chatId, String userName) {
        logger.info("The method 'stopCommand' was called");
        String text = "Спасибо, что ты был(а) со мной, " + userName + "! До скорых встреч!";
        sendMessage(chatId, text);
    }

}


