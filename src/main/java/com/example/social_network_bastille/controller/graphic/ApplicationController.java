package com.example.social_network_bastille.controller.graphic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    @FXML
    private Button btnLogout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogout.setOnAction(event -> DatabaseUserConnection.changeScene(event,
                "/view/log-in.fxml", null));
    }
}
