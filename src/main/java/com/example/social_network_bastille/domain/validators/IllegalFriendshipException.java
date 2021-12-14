package com.example.social_network_bastille.domain.validators;

public class IllegalFriendshipException extends Exception {
    public IllegalFriendshipException(String message) {
        super(message);
    }
}
