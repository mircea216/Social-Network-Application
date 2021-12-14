package com.example.social_network_bastille.service;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.graph.Graph;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface FriendshipServiceInterface {
    Friendship saveFriendship(Friendship friendship) throws IllegalFriendshipException;

    Friendship deleteFriendship(Tuple<Long, Long> id) throws IllegalFriendshipException;

    Iterable<Friendship> findAll();

    Friendship getFriendshipByID(Tuple<Long, Long> id);

    Graph generateGraphOfCommunities();

    Map<Long, List<Long>> loadUsersAndFriends();

    int countTheNumberOfCommunitiesInTheNetwork();

    HashSet<Integer> getTheMostNumerousCommunityOfFriends();

    List<String> displayFriendshipsOfGivenUser(User user);

    List<Friendship> displayFriendshipOfGivenMonth(int month, int year);
}
