package com.example.social_network_bastille.controller.graphic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    @FXML
    private Button btnFriendRequests;
    @FXML
    private Button btnChat;
    @FXML
    private Button btnMyFriends;
    @FXML
    private Button btnSearcher;
    @FXML
    private Button btnLogout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogout.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/log-in.fxml", null));
        InputStream inputSearch = getClass().getResourceAsStream("/images/search.png");
        assert inputSearch != null;
        Image searchImage= new Image(inputSearch, 30,30, true, true);
        btnSearcher.setBackground(Background.EMPTY);
        btnSearcher.setGraphic(new ImageView(searchImage));
        btnSearcher.setOnAction((event -> DatabaseUserConnection.changeScene(event,
                        "/view/found-user.fxml", null)));

        InputStream inputFriends = getClass().getResourceAsStream("/images/friends.png");
        assert inputFriends != null;
        Image friendsImage= new Image(inputFriends, 30,30, true, true);
        btnMyFriends.setBackground(Background.EMPTY);
        btnMyFriends.setGraphic(new ImageView(friendsImage));

        InputStream inputFriendRequest = getClass().getResourceAsStream("/images/friend-request.png");
        assert inputFriendRequest != null;
        Image friendRequestImage= new Image(inputFriendRequest, 30,30, true, true);
        btnFriendRequests.setBackground(Background.EMPTY);
        btnFriendRequests.setGraphic(new ImageView(friendRequestImage));

        InputStream inputChat = getClass().getResourceAsStream("/images/chat.png");
        assert inputChat != null;
        Image chatImage= new Image(inputChat, 30,30, true, true);
        btnChat.setBackground(Background.EMPTY);
        btnChat.setGraphic(new ImageView(chatImage));
    }
}
