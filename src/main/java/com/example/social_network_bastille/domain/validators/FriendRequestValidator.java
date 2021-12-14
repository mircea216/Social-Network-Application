package com.example.social_network_bastille.domain.validators;

import com.example.social_network_bastille.domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest> {
    private static final String NEWLINE = "\n";

    @Override
    public void validate(FriendRequest friendRequest) throws ValidationException {
        StringBuilder errorMessage = new StringBuilder();
        if (friendRequest.getFromUser() == null || friendRequest.getToUser() == null) {
            errorMessage.append("Users must exist" + NEWLINE);
        }
        if (!errorMessage.isEmpty())
            throw new ValidationException(errorMessage.toString());
    }
}
