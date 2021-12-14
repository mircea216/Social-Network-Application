package com.example.social_network_bastille.controller.implementation;

import com.example.social_network_bastille.controller.FriendRequestControllerInterface;
import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.implementation.FriendRequestService;

public class FriendRequestController implements FriendRequestControllerInterface {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @Override
    public FriendRequest saveFriendRequest(FriendRequest friendRequest) throws IllegalFriendshipException {
        return friendRequestService.saveFriendRequest(friendRequest);
    }

    @Override
    public FriendRequest deleteFriendRequest(Tuple<Long, Long> id) throws IllegalFriendshipException {
        return friendRequestService.deleteFriendRequest(id);
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        return friendRequestService.findAll();
    }

    @Override
    public FriendRequest getFriendRequestById(Tuple<Long, Long> id) {
        return friendRequestService.getFriendRequestById(id);
    }

    @Override
    public FriendRequest updateFriendRequest(FriendRequest friendRequest) {
        return friendRequestService.updateFriendRequest(friendRequest);
    }
}
