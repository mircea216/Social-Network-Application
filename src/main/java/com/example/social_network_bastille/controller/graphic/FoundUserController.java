package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.*;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.*;
import com.example.social_network_bastille.service.UserServiceInterface;
import com.example.social_network_bastille.service.implementation.UserService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FoundUserController implements Initializable {
    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";
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
    private final ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    public TableColumn<User,String>  lastNameCol;
    @FXML
    public TableColumn<User,String> firstNameCol;
    @FXML
    public TableView<User> foundUsers;
    @FXML
    public TextField tfSearch;
    @FXML
    public Button btnSearcher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputSearch = getClass().getResourceAsStream("/images/search.png");
        assert inputSearch != null;
        Image searchImage= new Image(inputSearch, 30,30, true, true);
        btnSearcher.setBackground(Background.EMPTY);
        btnSearcher.setGraphic(new ImageView(searchImage));


    }

    public void showUsers() {
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        foundUsers.setItems(users);
    }
    @FXML
    public void findAnUser() {
        Predicate<User> predicate = user -> (user.getFirstName() + " " + user.getLastName())
                .toLowerCase()
                .contains(tfSearch.getText().toLowerCase());
        ObservableList<User> filtered = FXCollections.observableArrayList();
        filtered.setAll(StreamSupport
                .stream(userService.findAll().spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList()));
        users.setAll(filtered);
        showUsers();
        foundUsers.setFixedCellSize(125);
        foundUsers.prefHeightProperty().bind(Bindings.size(foundUsers.getItems()).multiply(foundUsers.getFixedCellSize())
                .add(filtered.size()));
    }
}
