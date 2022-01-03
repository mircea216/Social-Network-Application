package com.example.social_network_bastille.controller.graphic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {
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
        InputStream inputMessage = getClass().getResourceAsStream("/images/send-message.png");
        assert inputMessage != null;
        Image sendMessage = new Image(inputMessage, 27, 27, true, true);
        button_send.setBackground(Background.EMPTY);
        button_send.setGraphic(new ImageView(sendMessage));

        vbox_messages.heightProperty().addListener((observable, oldValue, newValue) -> sp_main.setVvalue((Double)
                newValue));

        button_send.setOnAction(event -> {
            String sentMessage = tf_message.getText();
            if (!sentMessage.isEmpty()) {
                HBox chatBubble = new HBox();
                chatBubble.setAlignment(Pos.CENTER_RIGHT);
                chatBubble.setPadding(new Insets(5, 5, 5, 10));
                Text messageText = new Text(sentMessage);
                TextFlow flow = new TextFlow(messageText);

                flow.setStyle("-fx-background-color: #8B4513;" + "-fx-background-radius: 20px");
                flow.setPadding(new Insets(5, 5, 5, 10));

                messageText.setFill(Color.WHITE);
                chatBubble.getChildren().add(flow);
                vbox_messages.getChildren().add(chatBubble);

                tf_message.clear();
            }
        });
    }
}
