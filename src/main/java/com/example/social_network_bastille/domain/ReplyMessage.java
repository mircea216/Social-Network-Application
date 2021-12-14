package com.example.social_network_bastille.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message {
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private Message receivedMessage;

    public ReplyMessage(User from, List<User> to, String message, LocalDateTime date, Message receivedMessage) {
        super(from, to, message, date);
        this.receivedMessage = receivedMessage;
    }

    public Message getReceivedMessage() {
        return receivedMessage;
    }

    public void setReceivedMessage(Message receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(receivedMessage, that.receivedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), receivedMessage);
    }

    @Override
    public String toString() {
        if (receivedMessage != null) {
            if (getFrom() != null) {
                return "ID: " + getId() + NEW_LINE + "Reply message to: " + receivedMessage.getMessage() + " "
                        + NEW_LINE + "from: " + receivedMessage.getFrom().getFirstName() + SPACE
                        + receivedMessage.getFrom().getLastName() + NEW_LINE + "Reply text: " + getMessage();
            }
        }
        return "ID: " + getId();
    }
}
