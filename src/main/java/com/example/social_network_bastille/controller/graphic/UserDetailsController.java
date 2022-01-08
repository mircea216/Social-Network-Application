package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.controller.graphic.alert.AlertUtil;
import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserDetailsController implements Initializable {
    static User loggedUser;
    static User foundUser;
    @FXML
    private Button btnSendProfileMessage;
    @FXML
    private TextField tfSend;

    @FXML
    private Label labelUser;
    @FXML
    private Button btnAddDelete;
    @FXML
    private ImageView imgViewProfilePicture;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnSeeFriends;
    @FXML
    private TableView<User> tvFriends;
    @FXML
    private TableColumn<User, String> firstNameCol;
    @FXML
    private TableColumn<User, String> lastNameCol;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    @FXML
    private Button btnCloseTable;
    @FXML
    private Button btnMessage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnMessage.setVisible(true);
        InputStream inputPlane = getClass().getResourceAsStream("/images/email.png");
        assert inputPlane != null;
        Image sendImage = new Image(inputPlane, 25, 25, true, true);
        btnSendProfileMessage.setBackground(Background.EMPTY);
        btnSendProfileMessage.setGraphic(new ImageView(sendImage));
        btnSendProfileMessage.setVisible(false);
        InputStream inputCloseTable = getClass().getResourceAsStream("/images/close.png");
        assert inputCloseTable != null;
        Image closeTableImage = new Image(inputCloseTable, 30, 30, true, true);
        btnCloseTable.setBackground(Background.EMPTY);
        btnCloseTable.setGraphic(new ImageView(closeTableImage));
        btnCloseTable.setVisible(false);
        tfSend.setVisible(false);
        tvFriends.setVisible(false);
        onSeeFriendsButtonClick();
        loggedUser = FoundUserController.getLoggedUser();
        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBack.setBackground(Background.EMPTY);
        btnBack.setGraphic(new ImageView(backImage));
        btnBack.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/found-user.fxml", null));
        assert loggedUser != null;
        if (Objects.equals(loggedUser.getId(), foundUser.getId())) {
            labelUser.setText("Your" + " profile");
            btnAddDelete.setVisible(false);
            btnMessage.setVisible(false);
            InputStream inputImageSearcherProfile = getClass().getResourceAsStream("/images/searcherProfile.png");
            assert inputImageSearcherProfile != null;
            Image profilesearcherImage = new Image(inputImageSearcherProfile, 100, 100, true, true);
            imgViewProfilePicture.setImage(profilesearcherImage);
        } else {
            labelUser.setText(FoundUserController.usersName + "'s" + " profile");
            InputStream inputImageProfile = getClass().getResourceAsStream("/images/profilePicture.png");
            assert inputImageProfile != null;
            Image profileImage = new Image(inputImageProfile, 100, 100, true, true);
            imgViewProfilePicture.setImage(profileImage);
        }
        setButtonLabel();
        btnMessage.setOnAction(event->onMessageButtonClick());
    }

    public void setButtonLabel() {
        Tuple<Long, Long> tuple = new Tuple<>(loggedUser.getId(), foundUser.getId());
        if (FoundUserController.friendshipService.getFriendshipByID(tuple) != null) {
            btnAddDelete.setText("Delete");
        } else if (FoundUserController.friendRequestService.getFriendRequestById(tuple) != null) {
            if (FoundUserController.friendRequestService.getFriendRequestById(tuple).getStatus() == Status.PENDING) {
                btnAddDelete.setText("Cancel request");
            }
        }
    }

    public void onAddDeleteButtonClick() {
        Tuple<Long, Long> tuple = new Tuple<>(loggedUser.getId(), foundUser.getId());
        if (Objects.equals(btnAddDelete.getText(), "Delete")) {
            try {
                FoundUserController.friendshipService.deleteFriendship(tuple);
                btnAddDelete.setText("Add");
            } catch (IllegalFriendshipException e) {
                e.printStackTrace();
            }
        } else if (Objects.equals(btnAddDelete.getText(), "Add")) {
            btnAddDelete.setText("Cancel request");
            try {
                FriendRequest friendRequest = new FriendRequest(loggedUser, foundUser, LocalDateTime.now());
                FoundUserController.friendRequestService.saveFriendRequest(friendRequest);
            } catch (IllegalFriendshipException e) {
                e.printStackTrace();
            }
        } else if (Objects.equals(btnAddDelete.getText(), "Cancel request")) {
            btnAddDelete.setText("Add");
            try {
                FoundUserController.friendRequestService.deleteFriendRequest(tuple);
            } catch (IllegalFriendshipException e) {
                e.printStackTrace();
            }
            btnAddDelete.setText("Add");
        }
    }

    public void showUsers() {
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tvFriends.setItems(friends);
    }

    @FXML
    public void findFriends() {
        tvFriends.setVisible(true);
        Predicate<User> predicate = user -> (FoundUserController.friendshipService.
                getFriendshipByID(new Tuple<>(foundUser.getId(), user.getId())) != null);
        ObservableList<User> filtered = FXCollections.observableArrayList();
        filtered.setAll(StreamSupport
                .stream(FoundUserController.userService.findAll().spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList()));
        friends.setAll(filtered);
        if (filtered.size() == 0) {
            Label label = new Label("This user has no friends for now! ");
            Font font = Font.font("Harlow Solid Italic", FontWeight.BOLD, 17);
            label.setFont(font);
            tvFriends.setPlaceholder(label);
        }
        else {
            showUsers();
            tvFriends.setFixedCellSize(125);
        }
    }

    public void onSeeFriendsButtonClick() {
        btnSeeFriends.setOnAction(event -> {
            btnCloseTable.setVisible(true);
            findFriends();
            onCloseTableButtonClick();
        });

    }

    public void onCloseTableButtonClick() {
        btnCloseTable.setVisible(true);
        btnCloseTable.setCursor(Cursor.HAND);
        btnCloseTable.setOnMouseClicked(event -> {
            tvFriends.setVisible(false);
            btnCloseTable.setVisible(false);
        });
    }

    public void onMessageButtonClick() {
        btnMessage.setVisible(false);
        tfSend.setVisible(true);
        btnSendProfileMessage.setVisible(true);
        btnSendProfileMessage.setOnAction(event->{
            if(!tfSend.getText().isEmpty()) {
                Message message = new Message(loggedUser, List.of(foundUser), tfSend.getText(), LocalDateTime.now());
                try {
                    FoundUserController.messageService.saveMessage(message);
                    AlertUtil.showNotification("Your message has been sent!");
                    tfSend.clear();
                    btnMessage.setVisible(true);
                    tfSend.setVisible(false);
                    btnSendProfileMessage.setVisible(false);
                } catch (IllegalFriendshipException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
