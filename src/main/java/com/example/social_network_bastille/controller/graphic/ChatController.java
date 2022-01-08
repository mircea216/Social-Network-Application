package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.controller.graphic.alert.AlertUtil;
import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.dto.UserDTO;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class ChatController implements Initializable {
    private static final String SPACE = " ";

    static User loggedUser;
    static User recipientUser;
    public static String usersName;
    public Button btnGroupMessage;
    public TextField messageField;
    public Button sendBtn;
    public Button btnAddRecipients;
    public Label lblGroupMessage;
    List<User> to = new ArrayList<>();
    @FXML
    private Button btnBackChat;
    Stage stage;
    @FXML
    private TableView<UserDTO> tvRecipients;
    @FXML
    private TableColumn<UserDTO, String> colRecipients;
    public AnchorPane selectedRecipient;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblGroupMessage.setVisible(true);
        InputStream inputAddReceiver = getClass().getResourceAsStream("/images/add-receiver.png");
        assert inputAddReceiver != null;
        Image addReceiver = new Image(inputAddReceiver, 50, 50, true, true);
        btnAddRecipients.setBackground(Background.EMPTY);
        btnAddRecipients.setGraphic(new ImageView(addReceiver));

        InputStream inputSend = getClass().getResourceAsStream("/images/paper-plane.png");
        assert inputSend != null;
        Image send = new Image(inputSend, 25, 25, true, true);
        sendBtn.setBackground(Background.EMPTY);
        sendBtn.setGraphic(new ImageView(send));

        InputStream inputGroup = getClass().getResourceAsStream("/images/chat-group.png");
        assert inputGroup != null;
        Image group = new Image(inputGroup, 50, 50, true, true);
        btnGroupMessage.setBackground(Background.EMPTY);
        btnGroupMessage.setGraphic(new ImageView(group));


        btnAddRecipients.setVisible(false);
        messageField.setVisible(false);
        sendBtn.setVisible(false);
        InputStream inputBack = getClass().getResourceAsStream("/images/back.png");
        assert inputBack != null;
        Image backImage = new Image(inputBack, 30, 30, true, true);
        btnBackChat.setBackground(Background.EMPTY);
        btnBackChat.setGraphic(new ImageView(backImage));
        btnBackChat.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/app-page.fxml", null));
        loggedUser = FoundUserController.getLoggedUser();
        btnGroupMessage.setOnAction(event -> onBtnGroupMessage());
        addRecipients();
        getClickedRecipient();
    }

    private void showRecipients() {
        colRecipients.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    public void addRecipients() {
        ObservableList<UserDTO> filtered = FXCollections.observableArrayList();
        Set<UserDTO> setUserDTO = new HashSet<>();
        for (Message message : FoundUserController.messageService.findAll()) {
            if (Objects.equals(message.getFrom().getId(), loggedUser.getId())) {
                for (User user : message.getTo()) {
                    UserDTO userDTO = new UserDTO(user.getFirstName()
                            + SPACE + user.getLastName(), user.getId());
                    setUserDTO.add(userDTO);
                }
            }
            if (isRecipient(message)) {
                UserDTO userDTO = new UserDTO(message.getFrom().getFirstName()
                        + SPACE + message.getFrom().getLastName(), message.getFrom().getId());
                setUserDTO.add(userDTO);
            }
        }
        filtered.setAll(setUserDTO);
        tvRecipients.setItems(filtered);
        tvRecipients.setFixedCellSize(100);
        tvRecipients.prefHeightProperty().bind(Bindings.size(tvRecipients.getItems())
                .multiply(tvRecipients.getFixedCellSize())
                .add(filtered.size()));
        showRecipients();
    }

    private boolean isRecipient(Message message) {
        for (User user : message.getTo()) {
            if (Objects.equals(user.getId(), loggedUser.getId())) {
                return true;
            }
        }
        return false;
    }

    private void getClickedRecipient() {
        tvRecipients.setRowFactory(tableView -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    stage = (Stage) selectedRecipient.getScene().getWindow();
                    stage.close();
                    usersName = row.getItem().getUsername();
                    Long idUserRecipient = row.getItem().getId();
                    recipientUser = FoundUserController.userService.getUserByID(idUserRecipient);
                    FXMLLoader fxmlLoader = new FXMLLoader(DatabaseUserConnection.class.getResource
                            ("/view/chat-box.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 316, 410);
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

    public void onAddButtonClick() {
        messageField.setVisible(true);
        sendBtn.setVisible(true);
        btnAddRecipients.setOnAction(event -> onAddButtonClick());
        tvRecipients.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        UserDTO userDTO = tvRecipients.getSelectionModel().getSelectedItem();
        if (!to.contains(FoundUserController.userService.getUserByID(userDTO.getId()))) {
            to.add(FoundUserController.userService.getUserByID(userDTO.getId()));
        }
    }


    public void onBtnGroupMessage() {
        lblGroupMessage.setVisible(false);
        btnAddRecipients.setVisible(true);
        messageField.setVisible(true);
        sendBtn.setVisible(true);
        btnGroupMessage.setVisible(false);
        btnAddRecipients.setOnAction(event -> onAddButtonClick());
        tvRecipients.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sendBtn.setOnAction(event -> {
            String text = messageField.getText();
            if (!text.isEmpty()) {
                Message message = new Message(loggedUser, to, text, LocalDateTime.now());

                try {
                    FoundUserController.messageService.saveMessage(message);
                    AlertUtil.showNotification("Your message has been sent successfully to everyone!");
                    messageField.setVisible(false);
                    sendBtn.setVisible(false);
                    btnAddRecipients.setVisible(false);
                    btnGroupMessage.setVisible(true);
                    lblGroupMessage.setVisible(true);
                } catch (IllegalFriendshipException e) {
                    e.printStackTrace();
                }
            }
        });
        messageField.clear();
    }


}
