package com.example.social_network_bastille.service.implementation;

import com.example.social_network_bastille.domain.ReplyMessage;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.service.ReplyMessageServiceInterface;

public class ReplyMessageService implements ReplyMessageServiceInterface {
    private final Repository<Long, ReplyMessage> replyMessageRepository;

    public ReplyMessageService(Repository<Long, ReplyMessage> replyMessageRepository) {
        this.replyMessageRepository = replyMessageRepository;
    }

    @Override
    public ReplyMessage add(ReplyMessage replyMessage) throws IllegalFriendshipException {
        return replyMessageRepository.save(replyMessage);
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        return replyMessageRepository.findAll();
    }

    @Override
    public ReplyMessage getReplyMessageByID(Long id) {
        return replyMessageRepository.findOne(id);
    }

    @Override
    public ReplyMessage deleteReplyMessage(Long id) throws IllegalFriendshipException {
        return replyMessageRepository.delete(id);
    }
}
