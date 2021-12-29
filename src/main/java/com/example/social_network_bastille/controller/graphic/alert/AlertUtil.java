package com.example.social_network_bastille.controller.graphic.alert;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public static void showNotification(String notification) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NOTIFICATION");
        alert.setContentText(notification);
        alert.showAndWait();
    }
}
