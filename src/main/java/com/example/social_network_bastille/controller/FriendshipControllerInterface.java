package com.example.social_network_bastille.controller;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface FriendshipControllerInterface {
    Friendship saveFriendship(Friendship friendship) throws IllegalFriendshipException;

    Friendship deleteFriendship(Tuple<Long, Long> id) throws IllegalFriendshipException, IllegalArgumentException;

    Iterable<Friendship> findAll();

    Friendship getFriendshipByID(Tuple<Long, Long> id);

    Map<Long, List<Long>> loadUsersAndFriends();

    int countTheNumberOfCommunitiesInTheNetwork();

    HashSet<Integer> getTheMostNumerousCommunityOfFriends();

    List<String> displayFriendshipsOfGivenUser(User user);

    List<Friendship> displayFriendshipOfGivenMonth(int month,int year);
}
