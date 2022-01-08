package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.controller.FriendRequestControllerInterface;
import com.example.social_network_bastille.controller.UserControllerInterface;
import com.example.social_network_bastille.controller.graphic.alert.AlertUtil;
import com.example.social_network_bastille.controller.implementation.FriendRequestController;
import com.example.social_network_bastille.controller.implementation.UserController;
import com.example.social_network_bastille.domain.FriendRequest;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.dto.SentFriendRequestDTO;
import com.example.social_network_bastille.domain.validators.FriendRequestValidator;
import com.example.social_network_bastille.domain.validators.FriendshipValidator;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
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
import javafx.scene.control.Label;
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
import java.util.ResourceBundle;

public class SentFriendRequests implements Initializable {
    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Diamondfarm21";
    private static final String ALIGNMENT_CENTER = "-fx-alignment: CENTER;";
    public static final String TO_WHOM = "toWhom";
    public static final String DATE = "date";
    public static final String CANCEL = "cancel";


    @FXML
    private Button btnShow;
    @FXML
    private TableColumn<SentFriendRequestDTO, String> colToWhom;
    @FXML
    private TableColumn<SentFriendRequestDTO, String> colDate;
    @FXML
    private TableColumn<SentFriendRequestDTO, Button> colCancel;
    @FXML
    private TableView<SentFriendRequestDTO> tvSentRequests;
    @FXML
    private Button btnBackSentRequests;

    private final Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(), URL, USERNAME,
            PASSWORD);
    private final FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new
            FriendshipValidator(), URL, USERNAME, PASSWORD, userRepository);
    private final UserServiceInterface userService = new UserService(userRepository, friendshipDatabaseRepository);
    private final Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository =
            new FriendRequestDatabaseRepository(new
                    FriendRequestValidator(), URL, USERNAME, PASSWORD, userRepository, friendshipDatabaseRepository);
    private final FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
    private final UserControllerInterface userController = new UserController(userService);
    private final FriendRequestControllerInterface friendRequestController =
            new FriendRequestController(friendRequestService);
    private final ObservableList<SentFriendRequestDTO> sentFriendRequests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBackSentRequests.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/friend-requests.fxml", null));
        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBackSentRequests.setBackground(Background.EMPTY);
        btnBackSentRequests.setGraphic(new ImageView(backImage));

        InputStream inputShow = getClass().getResourceAsStream("/images/view.png");
        assert inputShow != null;
        Image viewImage = new Image(inputShow, 40, 40, true, true);
        btnShow.setBackground(Background.EMPTY);
        btnShow.setGraphic(new ImageView(viewImage));
        showFriendRequests();
    }

    public void showFriendRequests() {
        createTableView();
        int size = friendRequestController.getSentFriendRequests(
                userController.getUserByEmail(LogInController.getLoggedUserEmail()).getId()).size();
        tvSentRequests.setFixedCellSize(100);
        if (size == 0) {
            Label label = new Label("You have sent no requests! ");
            Font font = Font.font("Harlow Solid Italic", FontWeight.BOLD, 17);
            label.setFont(font);
            tvSentRequests.setPlaceholder(label);
        } else {
            tvSentRequests
                    .prefHeightProperty()
                    .bind(Bindings.size(tvSentRequests.getItems())
                            .multiply(tvSentRequests.getFixedCellSize())
                            .add(size));
        }
    }

    public void manageRequests() {
        SentFriendRequestDTO friendRequestDTO = tvSentRequests.getSelectionModel().getSelectedItem();
        Button cancel = friendRequestDTO.getCancel();
        handleCancelRequest(friendRequestDTO, cancel);

    }

    private void handleCancelRequest(SentFriendRequestDTO friendRequestDTO, Button cancel) {
        cancel.setOnAction(event -> {
            User currentUser = userController.getUserByEmail(LogInController.getLoggedUserEmail());
            Tuple<Long, Long> id = new Tuple<>(currentUser.getId(), friendRequestDTO.getToWhomID());
            try {
                friendRequestController.deleteFriendRequest(id);
            } catch (IllegalFriendshipException e) {
                e.printStackTrace();
            }
            tvSentRequests.getItems().remove(friendRequestDTO);
            AlertUtil.showNotification("You unsent your request for " + friendRequestDTO.getToWhom() + "!");
        });
    }

    private void createTableView() {
        User user = userController.getUserByEmail(LogInController.getLoggedUserEmail());
        for (FriendRequest friendRequest :
                friendRequestController.getSentFriendRequests(user.getId())) {
            String usernameToWhom = userController.getUserByID(friendRequest.getId().getId2()).getFirstName() + " " +
                    userController.getUserByID(friendRequest.getId().getId2()).getLastName();
            String date = DateFormatter.getFormattedLocalDateTime(friendRequest.getDate());
            Font font = Font.font("Courier New", FontWeight.BOLD, 13);
            Button cancel = new Button("Cancel");
            cancel.setStyle("-fx-background-color: #F4EEA9;" +
                    " -fx-border-color: white; -fx-border-radius: 5;" +
                    ALIGNMENT_CENTER +
                    "-fx-text-fill: black");
            cancel.setFont(font);
            cancel.setCursor(Cursor.HAND);
            cancel.setAlignment(Pos.CENTER);
            Long toWhomID = friendRequest.getId().getId2();
            SentFriendRequestDTO friendRequestDTO = new SentFriendRequestDTO(usernameToWhom, date, cancel, toWhomID);
            sentFriendRequests.add(friendRequestDTO);
        }
        colToWhom.setCellValueFactory(new PropertyValueFactory<>(TO_WHOM));
        colToWhom.setStyle(ALIGNMENT_CENTER);
        colDate.setCellValueFactory(new PropertyValueFactory<>(DATE));
        colDate.setStyle(ALIGNMENT_CENTER);
        colCancel.setCellValueFactory(new PropertyValueFactory<>(CANCEL));
        colCancel.setStyle(ALIGNMENT_CENTER);
        tvSentRequests.setItems(sentFriendRequests);
    }
}
