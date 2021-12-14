package com.example.social_network_bastille.controller.implementation;

import com.example.social_network_bastille.controller.FriendshipControllerInterface;
import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.FriendshipServiceInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FriendshipController implements FriendshipControllerInterface {
    private final FriendshipServiceInterface friendshipService;

    public FriendshipController(FriendshipServiceInterface friendshipService) {
        this.friendshipService = friendshipService;
    }

    @Override
    public Friendship saveFriendship(Friendship friendship) throws IllegalFriendshipException {
        return friendshipService.saveFriendship(friendship);
    }

    @Override
    public Friendship deleteFriendship(Tuple<Long, Long> id) throws IllegalFriendshipException, IllegalArgumentException {
        return friendshipService.deleteFriendship(id);
    }

    @Override
    public Iterable<Friendship> findAll() {
        return friendshipService.findAll();
    }

    @Override
    public Friendship getFriendshipByID(Tuple<Long, Long> id) {
        return friendshipService.getFriendshipByID(id);
    }

    @Override
    public Map<Long, List<Long>> loadUsersAndFriends() {
        return friendshipService.loadUsersAndFriends();
    }

    @Override
    public int countTheNumberOfCommunitiesInTheNetwork() {
        return friendshipService.countTheNumberOfCommunitiesInTheNetwork();
    }

    @Override
    public HashSet<Integer> getTheMostNumerousCommunityOfFriends() {
        return friendshipService.getTheMostNumerousCommunityOfFriends();
    }

    @Override
    public List<String> displayFriendshipsOfGivenUser(User user) {
        return friendshipService.displayFriendshipsOfGivenUser(user);
    }

    public List<Friendship> displayFriendshipOfGivenMonth(int month, int year) {
        return friendshipService.displayFriendshipOfGivenMonth(month, year);
    }
}
