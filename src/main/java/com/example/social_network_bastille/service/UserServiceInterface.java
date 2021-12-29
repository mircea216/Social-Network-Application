package com.example.social_network_bastille.service;

import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

public interface UserServiceInterface {
    User saveUser(User user) throws IllegalFriendshipException;

    User updateUser(User user);

    User deleteUser(Long id) throws IllegalFriendshipException;

    Iterable<User> findAll();

    User getUserByID(Long id);

    User getUserByEmail(String email);
}
