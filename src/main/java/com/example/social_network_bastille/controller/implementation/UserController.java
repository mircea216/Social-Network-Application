package com.example.social_network_bastille.controller.implementation;

import com.example.social_network_bastille.controller.UserControllerInterface;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.UserServiceInterface;

public class UserController implements UserControllerInterface {
    private final UserServiceInterface userService;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @Override
    public User saveUser(User user) throws IllegalFriendshipException {
        return userService.saveUser(user);
    }

    @Override
    public User deleteUser(Long id) throws IllegalFriendshipException {
        return userService.deleteUser(id);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userService.findAll();
    }

    @Override
    public User getUserByID(Long id) {
        return userService.getUserByID(id);
    }
}
