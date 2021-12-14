package com.example.social_network_bastille.service;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

public interface MessageServiceInterface {
    Message saveMessage(Message message) throws IllegalFriendshipException;

    Message getMessageByID(Long id);

    Iterable<Message> findAll();

    Message deleteMessage(Long id) throws IllegalFriendshipException;
}
