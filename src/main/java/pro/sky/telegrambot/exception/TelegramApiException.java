package pro.sky.telegrambot.exception;

public class TelegramApiException extends RuntimeException{

    public TelegramApiException(String message) {
        super(message);
    }
}
