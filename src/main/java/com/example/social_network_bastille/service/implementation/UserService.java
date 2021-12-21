package com.example.social_network_bastille.service.implementation;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.service.UserServiceInterface;

import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class UserService implements UserServiceInterface {
    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>,
            Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public User saveUser(User user) throws IllegalFriendshipException {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public User deleteUser(Long id) throws IllegalFriendshipException, ConcurrentModificationException {
        for (Friendship friendship : friendshipRepository.findAll()) {
            if (friendship.getId().getId1().equals(id) || friendship.getId().getId2().equals(id)) {
                friendshipRepository.delete(friendship.getId());
            }
        }
        return userRepository.delete(id);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getAccount().getEmail().equals(email))
                .collect(Collectors.toList())
                .get(0);
    }
}