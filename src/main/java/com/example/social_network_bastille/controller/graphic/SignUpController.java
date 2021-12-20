package com.example.social_network_bastille.controller.graphic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.social_network_bastille.controller.graphic.alert.AlertUtil.showError;

public class SignUpController implements Initializable {

    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfUserNameEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnLogIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignUp.setOnAction(event -> {
            if (!tfFirstName.getText().trim().isEmpty() && !tfLastName.getText().trim().isEmpty()
                    && !tfUserNameEmail.getText().trim().isEmpty() && !pfPassword.getText().trim().isEmpty()) {
                DatabaseUserConnection.signUpUser(event, tfFirstName.getText(), tfLastName.getText(),
                        tfUserNameEmail.getText(), pfPassword.getText());
            } else {
                showError("Please introduce all the information to sign up!");
            }
        });
        btnLogIn.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/log-in.fxml", null));
    }
}
