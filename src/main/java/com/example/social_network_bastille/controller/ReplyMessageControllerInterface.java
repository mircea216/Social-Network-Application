package com.example.social_network_bastille.controller;

import com.example.social_network_bastille.domain.ReplyMessage;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

public interface ReplyMessageControllerInterface {
    ReplyMessage add(ReplyMessage replyMessage) throws IllegalFriendshipException;

    Iterable<ReplyMessage> findAll();

    ReplyMessage getReplyMessageByID(Long id);

    ReplyMessage deleteReplyMessage(Long id) throws IllegalFriendshipException;
}
