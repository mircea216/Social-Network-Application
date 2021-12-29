package com.example.social_network_bastille.controller.graphic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private TextField tfUsername;
    @FXML
    private Button btnLogIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private PasswordField pfPassword;
    private static String loggedUserEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseUserConnection.logInUser(event, tfUsername.getText(), pfPassword.getText());
                loggedUserEmail = tfUsername.getText();
            }
        });

        btnSignUp.setOnAction(event -> DatabaseUserConnection.
                changeScene(event, "/view/sign-up.fxml", null));
    }

    public static String getLoggedUserEmail() {
        return loggedUserEmail;
    }

}
