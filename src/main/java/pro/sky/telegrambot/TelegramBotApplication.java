package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@EnableScheduling
@ComponentScan("pro.sky.telegrambot")
public class TelegramBotApplication {
	private static final Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

	@Autowired
	TelegramBotUpdatesListener listener = new TelegramBotUpdatesListener();

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

	@Scheduled(cron = "0 0/1 * * * *")
	public void sendNotifications() {
		Long chatId = 614749807L;
		try {
			listener.sendScheduledNotifications(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		} catch (Exception e) {
			logger.error("Ошибка при отправке уведомлений", e);
		}
	}
}
