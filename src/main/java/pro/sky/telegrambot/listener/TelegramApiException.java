package pro.sky.telegrambot.listener;

public class TelegramApiException extends RuntimeException{

    public TelegramApiException(String message) {
        super(message);
    }
}
