package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "notification_task")
public class Notification {

    public Notification() {
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

   @Column(name = "chatid")
    private Long chatId;

    private String message;
    private LocalDateTime datetime;

    public Notification(Long chatId, LocalDateTime datetime, String message) {
        this.chatId = chatId;
        this.datetime = datetime;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(datetime, that.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, datetime);
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDate(LocalDateTime dateTime) {
        this.datetime = dateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return id +". " + message + " " + dtf.format(datetime);
    }
}
