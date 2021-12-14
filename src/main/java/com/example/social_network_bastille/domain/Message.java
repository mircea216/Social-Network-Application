package com.example.social_network_bastille.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDateTime;


public class Message extends Entity<Long> {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;

    public Message(User from, List<User> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message1)) return false;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) &&
                Objects.equals(message, message1.message) && Objects.equals(date, message1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date);
    }

    @Override
    public String toString() {
        StringBuilder toFriends = new StringBuilder();
        if (!to.isEmpty()) {
            for (User user : to) {
                toFriends.append(user.getFirstName()).append(SPACE).append(user.getLastName()).append(NEW_LINE);
            }
            if (from != null) {
                return "ID: " + getId() + NEW_LINE + "FROM: " + from.getFirstName() + SPACE + from.getLastName()
                        + NEW_LINE + "TO: " + toFriends + "MESSAGE: " + message + NEW_LINE + "DATE: " +
                        getFormattedLocalDateTime(date);
            }
        }
        return "ID: " + getId() + " MESSAGE: " + message + NEW_LINE + "DATE: " + getFormattedLocalDateTime(date);
    }
}