module com.example.social_network_bastille {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.social_network_bastille to javafx.fxml;
    exports com.example.social_network_bastille;
    opens com.example.social_network_bastille.controller.graphic to javafx.fxml;
    exports com.example.social_network_bastille.controller.graphic;
}