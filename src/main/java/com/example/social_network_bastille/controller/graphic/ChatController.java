package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.dto.UserDTO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ChatController implements Initializable {
    private static final String SPACE = " ";

    static User loggedUser;
    static User receiver;
    public static String usersName;
    public Button btnSendTo;
    Stage stage;
    @FXML
    private TableView<UserDTO> tvRecipients;
    @FXML
    private TableColumn<UserDTO, String> colRecipients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedUser = FoundUserController.getLoggedUser();
        addRecipients();
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

    public void onSendToButtonClick(ActionEvent actionEvent) {
        //ObservableList<UserDTO> recipients = tvRecipients.getSelectionModel().getSelectedItems();
        //pentru o lista cu mai mult de 1 user sa apara message sent
       // if (recipients != null) {

        //} else {
          //  showErrorMessage("You didn't select any user");
        //}
        //users.setAll(getUserList());
    }
}
