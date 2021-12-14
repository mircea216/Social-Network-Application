package com.example.social_network_bastille.service.implementation;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.service.MessageServiceInterface;

public class MessageService implements MessageServiceInterface {
    private final Repository<Long, Message> messageRepository;

    public MessageService(Repository<Long, Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message saveMessage(Message message) throws IllegalFriendshipException {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageByID(Long id) {
        return messageRepository.findOne(id);
    }

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message deleteMessage(Long id) throws IllegalFriendshipException {
        return messageRepository.delete(id);
    }
}
