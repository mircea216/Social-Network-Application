package com.example.social_network_bastille.service.implementation;

import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Status;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.service.FriendRequestServiceInterface;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class FriendRequestService implements FriendRequestServiceInterface {
    private final Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository;

    public FriendRequestService(Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public FriendRequest saveFriendRequest(FriendRequest friendRequest) throws IllegalFriendshipException {
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    public FriendRequest deleteFriendRequest(Tuple<Long, Long> id) throws IllegalFriendshipException {
        return friendRequestRepository.delete(id);
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        return friendRequestRepository.findAll();
    }

    @Override
    public FriendRequest getFriendRequestById(Tuple<Long, Long> id) {
        return friendRequestRepository.findOne(id);
    }

    @Override
    public FriendRequest updateFriendRequest(FriendRequest friendRequest) {
        return friendRequestRepository.update(friendRequest);
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequests(Long secondID) {
        return StreamSupport
                .stream(friendRequestRepository.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getId().getId2().equals(secondID)
                        && friendRequest.getStatus().equals(Status.PENDING))
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequest> getSentFriendRequests(Long firstID) {
        return StreamSupport
                .stream(friendRequestRepository.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getId().getId1().equals(firstID)
                        && friendRequest.getStatus().equals(Status.PENDING))
                .collect(Collectors.toList());

    }
}
