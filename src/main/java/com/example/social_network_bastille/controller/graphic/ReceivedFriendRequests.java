package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.controller.FriendRequestControllerInterface;
import com.example.social_network_bastille.controller.UserControllerInterface;
import com.example.social_network_bastille.controller.graphic.alert.AlertUtil;
import com.example.social_network_bastille.controller.implementation.FriendRequestController;
import com.example.social_network_bastille.controller.implementation.UserController;
import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Status;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.dto.ReceivedFriendRequestDTO;
import com.example.social_network_bastille.domain.validators.FriendRequestValidator;
import com.example.social_network_bastille.domain.validators.FriendshipValidator;
import com.example.social_network_bastille.domain.validators.UserValidator;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.FriendRequestDatabaseRepository;
import com.example.social_network_bastille.repository.database.FriendshipDatabaseRepository;
import com.example.social_network_bastille.repository.database.UserDatabaseRepository;
import com.example.social_network_bastille.service.UserServiceInterface;
import com.example.social_network_bastille.service.implementation.FriendRequestService;
import com.example.social_network_bastille.service.implementation.UserService;
import com.example.social_network_bastille.utils.DateFormatter;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ReceivedFriendRequests implements Initializable {
    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";
    private static final String FROM = "from";
    private static final String DATE = "date";
    private static final String CONFIRM = "confirm";
    private static final String CANCEL = "cancel";
    private static final String ALIGNMENT_CENTER = "-fx-alignment: CENTER;";

    @FXML
    private Button btnShow;
    @FXML
    private Button btnBackReceivedRequests;
    @FXML
    private TableView<ReceivedFriendRequestDTO> tvReceivedRequests;
    @FXML
    private TableColumn<ReceivedFriendRequestDTO, String> colFrom;
    @FXML
    private TableColumn<ReceivedFriendRequestDTO, String> colDate;
    @FXML
    private TableColumn<ReceivedFriendRequestDTO, String> colConfirm;
    @FXML
    private TableColumn<ReceivedFriendRequestDTO, String> colCancel;
    private final ObservableList<ReceivedFriendRequestDTO> receivedFriendRequests = FXCollections.observableArrayList();


    private final Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(), URL, USERNAME,
            PASSWORD);
    private final FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new
            FriendshipValidator(), URL, USERNAME, PASSWORD, userRepository);
    private final UserServiceInterface userService = new UserService(userRepository, friendshipDatabaseRepository);
    private final Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository = new FriendRequestDatabaseRepository(new
            FriendRequestValidator(), URL, USERNAME, PASSWORD, userRepository, friendshipDatabaseRepository);
    private final FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
    private final UserControllerInterface userController = new UserController(userService);
    private final FriendRequestControllerInterface friendRequestController = new FriendRequestController(friendRequestService);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBackReceivedRequests.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/friend-requests.fxml", null));
        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBackReceivedRequests.setBackground(Background.EMPTY);
        btnBackReceivedRequests.setGraphic(new ImageView(backImage));

        InputStream inputShow = getClass().getResourceAsStream("/images/view.png");
        assert inputShow != null;
        Image viewImage = new Image(inputShow, 40, 40, true, true);
        btnShow.setBackground(Background.EMPTY);
        btnShow.setGraphic(new ImageView(viewImage));
        btnShow.setCursor(Cursor.HAND);
    }

    public void showFriendRequests() {
        createTableView();
        int size = friendRequestController.getReceivedFriendRequests(
                userController.getUserByEmail(LogInController.getLoggedUserEmail()).getId()).size();
        tvReceivedRequests.setFixedCellSize(100);
        tvReceivedRequests
                .prefHeightProperty()
                .bind(Bindings.size(tvReceivedRequests.getItems())
                        .multiply(tvReceivedRequests.getFixedCellSize())
                        .add(size));
    }

    public void manageRequests() {
        ReceivedFriendRequestDTO friendRequestDTO = tvReceivedRequests.getSelectionModel().getSelectedItem();
        Button confirm = friendRequestDTO.getConfirm();
        handleConfirmRequest(friendRequestDTO, confirm);
        handleDeclineRequest(friendRequestDTO);
    }

    private void handleDeclineRequest(ReceivedFriendRequestDTO friendRequestDTO) {
        friendRequestDTO.getCancel().setOnAction(event -> {
            User currentUser = userController.getUserByEmail(LogInController.getLoggedUserEmail());
            FriendRequest friendRequest = new FriendRequest(userController.getUserByID(
                    friendRequestDTO.getFirstID()),
                    userController.getUserByID(currentUser.getId()),
                    LocalDateTime.now());
            friendRequest.setStatus(Status.REJECTED);
            friendRequestController.updateFriendRequest(friendRequest);
            tvReceivedRequests.getItems().remove(friendRequestDTO);
            AlertUtil.showNotification("You declined " + friendRequestDTO.getFrom() + "'s request");
        });
    }

    private void handleConfirmRequest(ReceivedFriendRequestDTO friendRequestDTO, Button confirm) {
        confirm.setOnAction(event -> {
            User currentUser = userController.getUserByEmail(LogInController.getLoggedUserEmail());
            FriendRequest friendRequest = new FriendRequest(userController.getUserByID(
                    friendRequestDTO.getFirstID()),
                    userController.getUserByID(currentUser.getId()),
                    LocalDateTime.now());
            friendRequest.setStatus(Status.APPROVED);
            friendRequestController.updateFriendRequest(friendRequest);
            tvReceivedRequests.getItems().remove(friendRequestDTO);
            AlertUtil.showNotification("You accepted " + friendRequestDTO.getFrom() + "'s request");
        });
    }

    private void createTableView() {
        User user = userController.getUserByEmail(LogInController.getLoggedUserEmail());
        for (FriendRequest friendRequest :
                friendRequestController.getReceivedFriendRequests(user.getId())) {
            String username = userController.getUserByID(friendRequest.getId().getId1()).getFirstName() + " " +
                    userController.getUserByID(friendRequest.getId().getId1()).getLastName();
            String date = DateFormatter.getFormattedLocalDateTime(friendRequest.getDate());
            Button confirm = new Button("Confirm");
            Font font = Font.font("Courier New", FontWeight.BOLD, 13);
            confirm.setStyle("-fx-background-color: #F0BB62;" +
                    " -fx-border-color: white; -fx-border-radius: 5;" +
                    ALIGNMENT_CENTER +
                    "-fx-text-fill: white");
            confirm.setFont(font);
            confirm.setCursor(Cursor.HAND);
            confirm.setAlignment(Pos.CENTER);
            Button cancel = new Button("Decline");
            cancel.setStyle("-fx-background-color: #F4EEA9;" +
                    " -fx-border-color: white; -fx-border-radius: 5;" +
                    ALIGNMENT_CENTER +
                    "-fx-text-fill: black");
            cancel.setFont(font);
            cancel.setCursor(Cursor.HAND);
            cancel.setAlignment(Pos.CENTER);
            Long firstID = friendRequest.getId().getId1();
            ReceivedFriendRequestDTO friendRequestDTO = new ReceivedFriendRequestDTO(username, date, confirm, cancel,
                    firstID);
            receivedFriendRequests.add(friendRequestDTO);
        }
        colFrom.setCellValueFactory(new PropertyValueFactory<>(FROM));
        colFrom.setStyle(ALIGNMENT_CENTER);
        colDate.setCellValueFactory(new PropertyValueFactory<>(DATE));
        colDate.setStyle(ALIGNMENT_CENTER);
        colConfirm.setCellValueFactory(new PropertyValueFactory<>(CONFIRM));
        colConfirm.setStyle(ALIGNMENT_CENTER);
        colCancel.setCellValueFactory(new PropertyValueFactory<>(CANCEL));
        colCancel.setStyle(ALIGNMENT_CENTER);
        tvReceivedRequests.setItems(receivedFriendRequests);
    }
}
