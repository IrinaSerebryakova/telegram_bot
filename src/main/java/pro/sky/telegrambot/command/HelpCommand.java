package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.utils.CommandSupportUtils;

import static pro.sky.telegrambot.utils.CommandSupportUtils.chatId;

@Component
public class HelpCommand implements TelegramCommand {
    private static final String HELP = "/help";

    @Override
    public boolean support(Update update) {
        return CommandSupportUtils.isStringEqualsCommand(update, HELP);
    }

    @Override
    public SendMessage handle(Update update) {
        String text = "/start - начать общение с ботом\n" +
                "/help - показать список команд\n" +
                "/all - увидеть список напоминаний\n" +
                "Пример уведомления: 16.02.2025 12:28 Сделать домашнюю работу";
        return new SendMessage(chatId(update), text);
    }
}
