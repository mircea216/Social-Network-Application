package com.example.social_network_bastille.domain.validators;

import com.example.social_network_bastille.domain.ReplyMessage;

public class ReplyMessageValidator implements Validator<ReplyMessage> {
    private static final String NEWLINE = "\n";

    @Override
    public void validate(ReplyMessage entity) throws ValidationException {
        StringBuilder errorMessage = new StringBuilder();
        if (entity.getReceivedMessage() == null) {
            errorMessage.append("The received message cannot be empty!" + NEWLINE);
        }
        if (!errorMessage.isEmpty())
            throw new ValidationException(errorMessage.toString());
    }
}
