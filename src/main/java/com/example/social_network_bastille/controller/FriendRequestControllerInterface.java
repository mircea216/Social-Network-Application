package com.example.social_network_bastille.controller;

import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

import java.util.List;

public interface FriendRequestControllerInterface {
    FriendRequest saveFriendRequest(FriendRequest friendRequest) throws IllegalFriendshipException;

    FriendRequest deleteFriendRequest(Tuple<Long, Long> id) throws IllegalFriendshipException;

    Iterable<FriendRequest> findAll();

    FriendRequest getFriendRequestById(Tuple<Long, Long> id);

    FriendRequest updateFriendRequest(FriendRequest friendRequest);

    List<FriendRequest> getReceivedFriendRequests(Long secondID);
}
