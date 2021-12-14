package com.example.social_network_bastille.sample;

import com.example.social_network_bastille.controller.*;
import com.example.social_network_bastille.controller.implementation.*;
import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.*;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.*;
import com.example.social_network_bastille.service.FriendshipServiceInterface;
import com.example.social_network_bastille.service.MessageServiceInterface;
import com.example.social_network_bastille.service.ReplyMessageServiceInterface;
import com.example.social_network_bastille.service.UserServiceInterface;
import com.example.social_network_bastille.service.implementation.*;
import com.example.social_network_bastille.userinterface.UserInterface;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";

    public static void main(String[] args) {
        Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(), URL, USERNAME,
                PASSWORD);
        FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new
                FriendshipValidator(), URL, USERNAME, PASSWORD, userRepository);
        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(new MessageValidator(), URL, USERNAME,
                PASSWORD, userRepository);
        Repository<Long, ReplyMessage> replyMessageRepository = new ReplyMessageDatabaseRepository(new
                ReplyMessageValidator(), URL, USERNAME, PASSWORD, messageRepository, userRepository);
        Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository = new FriendRequestDatabaseRepository(new
                FriendRequestValidator(), URL, USERNAME, PASSWORD, userRepository, friendshipDatabaseRepository);
        UserServiceInterface userService = new UserService(userRepository, friendshipDatabaseRepository);
        FriendshipServiceInterface friendshipService = new FriendshipService(userRepository,
                friendshipDatabaseRepository);
        MessageServiceInterface messageService = new MessageService(messageRepository);
        ReplyMessageServiceInterface replyMessageService = new ReplyMessageService(replyMessageRepository);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
        UserControllerInterface userController = new UserController(userService);
        FriendshipControllerInterface friendshipController = new FriendshipController(friendshipService);
        MessageControllerInterface messageController = new MessageController(messageService);
        ReplyMessageControllerInterface replyMessageController = new ReplyMessageController(replyMessageService);
        FriendRequestControllerInterface friendRequestController = new FriendRequestController(friendRequestService);
        UserInterface userInterface = new UserInterface(userController, friendshipController, messageController,
                replyMessageController, friendRequestController);
        userInterface.run();
    }
}