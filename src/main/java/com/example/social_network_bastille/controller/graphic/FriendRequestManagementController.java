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

public class FriendRequestManagementController implements Initializable {
    @FXML
    private Button btnSentFriendRequests;
    @FXML
    private Button btnReceivedFriendRequests;
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBack.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/app-page.fxml", null));
        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBack.setBackground(Background.EMPTY);
        btnBack.setGraphic(new ImageView(backImage));

        InputStream inputSentFriendRequest = getClass().getResourceAsStream("/images/sent.png");
        assert inputSentFriendRequest != null;
        Image sentFriendRequestImage = new Image(inputSentFriendRequest, 35, 35,
                true, true);
        btnSentFriendRequests.setBackground(Background.EMPTY);
        btnSentFriendRequests.setGraphic(new ImageView(sentFriendRequestImage));

        InputStream inputReceivedFriendRequest = getClass().getResourceAsStream("/images/received.png");
        assert inputReceivedFriendRequest != null;
        Image ReceivedFriendRequestImage = new Image(inputReceivedFriendRequest, 40, 40,
                true, true);
        btnReceivedFriendRequests.setBackground(Background.EMPTY);
        btnReceivedFriendRequests.setGraphic(new ImageView(ReceivedFriendRequestImage));
        btnReceivedFriendRequests.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/received-friend-requests.fxml", null));
    }
}
