package com.example.social_network_bastille.service.implementation;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.graph.Graph;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.service.FriendshipServiceInterface;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDate;


public class FriendshipService implements FriendshipServiceInterface {
    private static final String SPACE = " ";
    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public FriendshipService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship>
            friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship saveFriendship(Friendship friendship) throws IllegalFriendshipException,
            IllegalArgumentException {
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship deleteFriendship(Tuple<Long, Long> id) throws IllegalFriendshipException,
            ConcurrentModificationException {
        return friendshipRepository.delete(id);
    }

    @Override
    public Iterable<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public Friendship getFriendshipByID(Tuple<Long, Long> id) {
        return friendshipRepository.findOne(id);
    }

    public Map<Long, List<Long>> loadUsersAndFriends() {
        Map<Long, List<Long>> userAndFriends = new HashMap<>();
        for (User user : userRepository.findAll()) {
            userAndFriends.put(user.getId(), new ArrayList<>());
        }
        for (Friendship friendship : friendshipRepository.findAll()) {
            userAndFriends.get(friendship.getId().getId1()).add(userRepository.
                    findOne(friendship.getId().getId2()).getId());
            userAndFriends.get(friendship.getId().getId2()).add(userRepository.
                    findOne(friendship.getId().getId1()).getId());
        }
        return userAndFriends;
    }

    @Override
    public Graph generateGraphOfCommunities() {
        Graph network = new Graph(userRepository.size());
        List<Integer> listOfIDs = new ArrayList<>();
        int index = 0, index1, index2;
        for (Friendship p : friendshipRepository.findAll()) {
            int firstVertex = p.getId().getId1().intValue();
            int secondVertex = p.getId().getId2().intValue();
            if (!listOfIDs.contains(firstVertex)) {
                index1 = index;
                index++;
                listOfIDs.add(firstVertex);
            } else {
                index1 = listOfIDs.lastIndexOf(firstVertex);
            }
            if (!listOfIDs.contains(secondVertex)) {
                index2 = index;
                index++;
                listOfIDs.add(secondVertex);
            } else {
                index2 = listOfIDs.lastIndexOf(secondVertex);
            }
            network.addEdge(index1, index2);
        }
        return network;
    }

    @Override
    public int countTheNumberOfCommunitiesInTheNetwork() {
        Graph network = generateGraphOfCommunities();
        return network.countTheNumberOfCommunitiesOfFriends();
    }

    @Override
    public HashSet<Integer> getTheMostNumerousCommunityOfFriends() {
        Graph network = generateGraphOfCommunities();
        return network.getTheMostNumerousCommunityOfFriends();
    }

    @Override
    public List<String> displayFriendshipsOfGivenUser(User user) {
        return StreamSupport
                .stream(friendshipRepository.findAll().spliterator(), false)
                .filter(friendship -> friendship.getId().getId1().equals(user.getId()) ||
                        friendship.getId().getId2().equals(user.getId()))
                .map(friendship -> {
                    User friend;
                    if (friendship.getId().getId1().equals(user.getId()))
                        friend = userRepository.findOne(friendship.getId().getId2());
                    else
                        friend = userRepository.findOne(friendship.getId().getId1());
                    return friend.getFirstName() + SPACE + friend.getLastName() + SPACE +
                            getFormattedLocalDate(friendship.getDate());
                })
                .collect(Collectors.toList());
    }

    public List<Friendship> displayFriendshipOfGivenMonth(int month, int year) {
        Predicate<Friendship> predicate = friendship -> (friendship.getDate().getMonthValue() == month)
                && (friendship.getDate().getYear() == year);
        return StreamSupport
                .stream(friendshipRepository.findAll().spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
