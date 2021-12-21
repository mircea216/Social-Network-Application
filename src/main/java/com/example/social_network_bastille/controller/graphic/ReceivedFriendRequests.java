package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.controller.FriendRequestControllerInterface;
import com.example.social_network_bastille.controller.UserControllerInterface;
import com.example.social_network_bastille.controller.implementation.FriendRequestController;
import com.example.social_network_bastille.controller.implementation.UserController;
import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.FriendRequestValidator;
import com.example.social_network_bastille.domain.validators.FriendshipValidator;
import com.example.social_network_bastille.domain.validators.UserValidator;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.FriendRequestDatabaseRepository;
import com.example.social_network_bastille.repository.database.FriendshipDatabaseRepository;
import com.example.social_network_bastille.repository.database.UserDatabaseRepository;
import com.example.social_network_bastille.service.FriendshipServiceInterface;
import com.example.social_network_bastille.service.UserServiceInterface;
import com.example.social_network_bastille.service.implementation.FriendRequestService;
import com.example.social_network_bastille.service.implementation.FriendshipService;
import com.example.social_network_bastille.service.implementation.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceivedFriendRequests implements Initializable {
    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";

    @FXML
    private Button btn;
    private final Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(), URL, USERNAME,
            PASSWORD);
    private final FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new
            FriendshipValidator(), URL, USERNAME, PASSWORD, userRepository);
    private final UserServiceInterface userService = new UserService(userRepository, friendshipDatabaseRepository);
    private final FriendshipServiceInterface friendshipService = new FriendshipService(userRepository,
            friendshipDatabaseRepository);
    private final Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository = new FriendRequestDatabaseRepository(new
            FriendRequestValidator(), URL, USERNAME, PASSWORD, userRepository, friendshipDatabaseRepository);
    private final FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
    private final UserControllerInterface userController = new UserController(userService);
    private final FriendRequestControllerInterface friendRequestController = new FriendRequestController(friendRequestService);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}
