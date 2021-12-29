package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.service.implementation.FriendRequestService;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserDetailsController implements Initializable {
    static User loggedUser;
    static User foundUser;

    @FXML
    public Label labelUser;
    public Button btnAddDelete;
    public ImageView imgViewProfilePicture;
    public Button btnBack;
    public Button btnSeeFriends;
    public AnchorPane scene2;
    public AnchorPane sceneToFade;
    public TableView<User> tvFriends;
    public TableColumn<User, String> firstNameCol;
    public TableColumn<User, String> lastNameCol;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    public Button btnCloseTable;
    public Button btnMessage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCloseTable.setVisible(false);
        btnCloseTable.setText("");
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
        showUsers();
        tvFriends.setFixedCellSize(125);
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
        btnCloseTable.setText("Close");
        btnCloseTable.setCursor(Cursor.HAND);
        btnCloseTable.setOnMouseClicked(event -> {
            tvFriends.setVisible(false);
            btnCloseTable.setVisible(false);
        });
    }
}
