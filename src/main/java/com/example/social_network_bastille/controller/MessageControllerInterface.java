package com.example.social_network_bastille.controller;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

public interface MessageControllerInterface {
    Message saveMessage(Message message) throws IllegalFriendshipException;

    Iterable<Message> findAll();

    Message getMessageByID(Long id);

    Message deleteMessage(Long id) throws IllegalFriendshipException;

}
