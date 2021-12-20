package com.example.social_network_bastille.controller.graphic;

import com.example.social_network_bastille.domain.Account;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.UserValidator;
import com.example.social_network_bastille.repository.Repository;
import com.example.social_network_bastille.repository.database.UserDatabaseRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.example.social_network_bastille.controller.graphic.alert.AlertUtil.showError;
import static com.example.social_network_bastille.utils.PasswordHashing.encrypt;

public class DatabaseUserConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/social_network";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Diamondfarm21";
    private static final String SELECT = "select " +
            "password from users where email = ?";
    private static final String SECRET = "SSH";
    private static final String ERROR_AUTHENTIFICATION = "The provided credentials are incorrect";
    private static final String WRONG_EMAIL = "No user with this email in the database";
    private static final String ALREADY_LOGGED = "There is already an user with this email";

    private static Repository<Long, User> userRepository;

    private static void setUserRepository(Repository<Long, User> userRepository) {
        DatabaseUserConnection.userRepository = userRepository;
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String username) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DatabaseUserConnection.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(DatabaseUserConnection.class.getResource(fxmlFile));
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        assert root != null;
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String firstName, String lastName, String email, String password) {
        Connection connection;
        PreparedStatement userExists;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            userExists = connection.prepareStatement("select * from users where email = ?");
            userExists.setString(1, email);
            ResultSet resultSet = userExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                showError(ALREADY_LOGGED);
            } else {
                User user = new User(firstName, lastName, new Account(email, password));
                try {
                    Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator(),
                            URL, USER, PASSWORD);
                    setUserRepository(userRepository);
                    userRepository.save(user);
                    changeScene(event, "/view/app-page.fxml", email);
                } catch (IllegalFriendshipException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logInUser(ActionEvent event, String email, String password) {
        try {
            Connection connection = DriverManager.getConnection(
                    URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println(WRONG_EMAIL);
                showError(ERROR_AUTHENTIFICATION);
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(encrypt(password, SECRET))) {
                        changeScene(event, "/view/app-page.fxml", email);
                    } else {
                        showError(ERROR_AUTHENTIFICATION);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
