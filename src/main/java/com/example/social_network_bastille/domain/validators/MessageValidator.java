package com.example.social_network_bastille.domain.validators;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.User;

public class MessageValidator implements Validator<Message> {
    private static final String NEWLINE = "\n";

    @Override
    public void validate(Message entity) throws ValidationException {
        StringBuilder errorMessage = new StringBuilder();
        if (entity.getMessage().isEmpty()) {
            errorMessage.append("Message cannot be empty!" + NEWLINE);
        }
        for (User recipient : entity.getTo()) {
            if (recipient.equals(entity.getFrom())) {
                errorMessage.append("You cannot send a message to yourself hehe!" + NEWLINE);
                break;
            }
        }
        if (entity.getFrom() == null) {
            errorMessage.append("User cannot be null!" + NEWLINE);
        }
        if (entity.getTo().isEmpty()) {
            errorMessage.append("User cannot be null!" + NEWLINE);
        }
        if (entity.getDate() == null) {
            errorMessage.append("Date cannot be null!" + NEWLINE);
        }
        if (!errorMessage.isEmpty())
            throw new ValidationException(errorMessage.toString());
    }
}
