package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.utils.CommandSupportUtils;

import static pro.sky.telegrambot.utils.CommandSupportUtils.chatId;
import static pro.sky.telegrambot.utils.CommandSupportUtils.userName;

@Component
public class StartCommand implements TelegramCommand{

    private static final String START = "/start";

    @Override
    public boolean support(Update update) {    // проверяем то, что нам прислал телеграм-бот: update.message().text()
        return CommandSupportUtils.isStringEqualsCommand(update, START);
    }

    @Override
    public SendMessage handle(Update update) {
        String text = "Добро пожаловать, " + userName(update) + "!";
        return new SendMessage(chatId(update), text);
    }
}
