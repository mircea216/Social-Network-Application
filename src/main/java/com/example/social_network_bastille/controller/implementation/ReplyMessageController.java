package com.example.social_network_bastille.controller.implementation;

import com.example.social_network_bastille.controller.ReplyMessageControllerInterface;
import com.example.social_network_bastille.domain.ReplyMessage;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.ReplyMessageServiceInterface;

public class ReplyMessageController implements ReplyMessageControllerInterface {
    private final ReplyMessageServiceInterface replyMessageService;

    public ReplyMessageController(ReplyMessageServiceInterface replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public ReplyMessage add(ReplyMessage replyMessage) throws IllegalFriendshipException {
        return replyMessageService.add(replyMessage);
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        return replyMessageService.findAll();
    }

    @Override
    public ReplyMessage getReplyMessageByID(Long id) {
        return replyMessageService.getReplyMessageByID(id);
    }

    @Override
    public ReplyMessage deleteReplyMessage(Long id) throws IllegalFriendshipException {
        return replyMessageService.deleteReplyMessage(id);
    }
}
