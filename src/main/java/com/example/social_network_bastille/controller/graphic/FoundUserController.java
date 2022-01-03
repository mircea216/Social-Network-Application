package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.*;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.*;
import com.example.social_network_bastille.service.UserServiceInterface;
import com.example.social_network_bastille.service.implementation.FriendRequestService;
import com.example.social_network_bastille.service.implementation.FriendshipService;
import com.example.social_network_bastille.service.implementation.MessageService;
import com.example.social_network_bastille.service.implementation.UserService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FoundUserController implements Initializable {
    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";
    @FXML
    public Button btnBack;
    public HBox foundUserScene;

    static Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(), URL, USERNAME,
            PASSWORD);
    static FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new
            FriendshipValidator(), URL, USERNAME, PASSWORD, userRepository);
    static FriendshipService friendshipService = new FriendshipService(userRepository, friendshipDatabaseRepository);
    static Repository<Long, Message> messageRepository = new MessageDatabaseRepository(new MessageValidator(), URL, USERNAME,
            PASSWORD, userRepository);
    static Repository<Long, ReplyMessage> replyMessageRepository = new ReplyMessageDatabaseRepository(new
            ReplyMessageValidator(), URL, USERNAME, PASSWORD, messageRepository, userRepository);
    static Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository = new FriendRequestDatabaseRepository(new
            FriendRequestValidator(), URL, USERNAME, PASSWORD, userRepository, friendshipDatabaseRepository);
    static UserServiceInterface userService = new UserService(userRepository, friendshipDatabaseRepository);
    static FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
    static MessageService messageService=new MessageService(messageRepository);
    private final ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    public TableColumn<User, String> lastNameCol;
    @FXML
    public TableColumn<User, String> firstNameCol;
    @FXML
    public TableView<User> foundUsers;
    @FXML
    public TextField tfSearch;
    @FXML
    public Button btnSearcher;
    public static String usersName;
    public ImageView imageSearch;
    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputSearch = getClass().getResourceAsStream("/images/search.png");
        assert inputSearch != null;
        Image searchImage = new Image(inputSearch, 20, 20, true, true);
        btnSearcher.setBackground(Background.EMPTY);
        btnSearcher.setGraphic(new ImageView(searchImage));

        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBack.setBackground(Background.EMPTY);
        btnBack.setGraphic(new ImageView(backImage));
        btnBack.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/app-page.fxml", null));

        InputStream inputImageSearch = getClass().getResourceAsStream("/images/searching.png");
        assert inputImageSearch != null;
        Image searchingImage = new Image(inputImageSearch, 100, 100, true, true);
        imageSearch.setImage(searchingImage);
        getClickedUser();
    }

    private void getClickedUser() {
        foundUsers.setRowFactory(tableView -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    stage = (Stage) foundUserScene.getScene().getWindow();
                    stage.close();
                    usersName = row.getItem().getFirstName();
                    UserDetailsController.foundUser = row.getItem();
                    FXMLLoader fxmlLoader = new FXMLLoader(DatabaseUserConnection.class.getResource("/view/user-details.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 600, 400);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(scene);
                    stage.show();
                }
            });
            return row;
        });
    }

    public static User getLoggedUser() {
        String loggedMail = LogInController.getLoggedUserEmail();
        Iterable<User> users = userService.findAll();
        for (User user : users) {
            if (Objects.equals(user.getAccount().getEmail(), loggedMail)) {
                return user;
            }
        }
        return null;
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
