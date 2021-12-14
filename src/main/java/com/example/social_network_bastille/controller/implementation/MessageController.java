package com.example.social_network_bastille.controller.implementation;

import com.example.social_network_bastille.controller.MessageControllerInterface;
import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.MessageServiceInterface;

public class MessageController implements MessageControllerInterface {
    private final MessageServiceInterface messageService;

    public MessageController(MessageServiceInterface messageService) {
        this.messageService = messageService;
    }

    @Override
    public Message saveMessage(Message message) throws IllegalFriendshipException {
        return messageService.saveMessage(message);
    }

    @Override
    public Iterable<Message> findAll() {
        return messageService.findAll();
    }

    @Override
    public Message getMessageByID(Long id) {
        return messageService.getMessageByID(id);
    }

    @Override
    public Message deleteMessage(Long id) throws IllegalFriendshipException {
        return messageService.deleteMessage(id);
    }
}
