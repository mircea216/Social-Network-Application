package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.ReplyMessage;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.w3c.dom.events.MouseEvent;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatBoxController implements Initializable {
    private static User loggedUser;


    private static User recipientUser;
    Long idMessage;
    @FXML
    private Button btnClose;
    @FXML
    private VBox vbox_messages;
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private ScrollPane sp_main;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClose.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/chat-page.fxml", null));
        loggedUser = FoundUserController.getLoggedUser();
        recipientUser = ChatController.recipientUser;
        InputStream inputMessage = getClass().getResourceAsStream("/images/send-message.png");
        assert inputMessage != null;
        Image sendMessage = new Image(inputMessage, 27, 27, true, true);
        button_send.setBackground(Background.EMPTY);
        button_send.setGraphic(new ImageView(sendMessage));

        InputStream inputClose = getClass().getResourceAsStream("/images/cancel.png");
        assert inputClose != null;
        Image close = new Image(inputClose, 20, 20, true, true);
        btnClose.setBackground(Background.EMPTY);
        btnClose.setGraphic(new ImageView(close));

        vbox_messages.heightProperty().addListener((observable, oldValue, newValue) -> sp_main.setVvalue((Double)
                newValue));
        List<Message> messageList = StreamSupport
                .stream(FoundUserController.messageService.findAll().spliterator(), false).sorted(new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                })
                .collect(Collectors.toList());
        for (Message message : messageList) {
            if (message.getFrom().getId().equals(loggedUser.getId()) && isRecipient(message, recipientUser)) {
                createChatBubble(message.getMessage(), Pos.CENTER_RIGHT, "-fx-background-color: #8B4513;" + "-fx-background-radius: 20px");
            }
            if (isRecipient(message, loggedUser) && message.getFrom().getId().equals(recipientUser.getId())) {
                createChatBubble(message.getMessage(), Pos.CENTER_LEFT, "-fx-background-color: #461111;" + "-fx-background-radius: 20px");
            }
        }

        button_send.setOnAction(event -> {
            String sentMessage = tf_message.getText();
            if (!sentMessage.isEmpty()) {
                createChatBubble(tf_message.getText(), Pos.CENTER_RIGHT, "-fx-background-color: #8B4513;" + "-fx-background-radius: 20px");
                Message messageToSave = new Message(loggedUser, List.of(recipientUser), tf_message.getText(), LocalDateTime.now());
                try {
                    FoundUserController.messageService.saveMessage(messageToSave);
                } catch (IllegalFriendshipException e) {
                    e.printStackTrace();
                }
                tf_message.clear();
            }
        });


    }

    private void onChatBubbleClick() {


        button_send.setOnAction(event -> {
            String sentMessage = "Replied TO: " + tf_message.getText();
            if (!tf_message.getText().isEmpty()) {
                createChatBubble(sentMessage, Pos.CENTER_RIGHT, "-fx-background-color: #0B4619;" +
                        "-fx-background-radius: 20px");
                Message replyMessage = new Message(loggedUser, List.of(recipientUser),
                        tf_message.getText(), LocalDateTime.now());
                try {
                    FoundUserController.messageService.saveMessage(replyMessage);
                } catch (IllegalFriendshipException e) {
                    e.printStackTrace();
                }
                tf_message.clear();
            }
        });
    }

    private void createChatBubble(String message, Pos position, String style) {

        HBox chatBubble = new HBox();
        chatBubble.setAlignment(position);
        chatBubble.setPadding(new Insets(5, 5, 5, 10));
        Text messageText = new Text(message);
        TextFlow flow = new TextFlow(messageText);

        flow.setStyle(style);
        flow.setPadding(new Insets(5, 5, 5, 10));

        messageText.setFill(Color.WHITE);
        chatBubble.getChildren().add(flow);
        vbox_messages.getChildren().add(chatBubble);

        chatBubble.setOnMouseClicked(event -> {
            chatBubble.setCursor(Cursor.OPEN_HAND);
            onChatBubbleClick();
        });
    }

    private boolean isRecipient(Message message, User userRecipient) {
        for (User user : message.getTo()) {
            if (Objects.equals(user.getId(), userRecipient.getId())) {
                return true;
            }
        }
        return false;
    }


}
