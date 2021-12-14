package com.example.social_network_bastille.domain.validators;

import com.example.social_network_bastille.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship friendship) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (friendship.getDate() == null) {
            errors.append("Invalid date! The date cannot be null!\n");
        }
        if (friendship.getId().getId1() == null || friendship.getId().getId2() == null) {
            errors.append("The IDs of the friendship cannot be null ");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }
}
