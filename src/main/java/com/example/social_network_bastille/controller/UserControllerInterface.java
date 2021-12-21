package com.example.social_network_bastille.controller;

import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;

public interface UserControllerInterface {
    User saveUser(User user) throws IllegalFriendshipException;

    User deleteUser(Long id) throws IllegalFriendshipException;

    User updateUser(User user);

    Iterable<User> findAll();

    User getUserByID(Long id);

    User getUserByEmail(String email);

}
