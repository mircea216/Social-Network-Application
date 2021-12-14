package com.example.social_network_bastille.repository.database;


import com.example.social_network_bastille.domain.Account;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static com.example.social_network_bastille.utils.PasswordHashing.encrypt;


public class UserDatabaseRepository extends AbstractDatabaseRepository<Long, User> {

    private static final String INSERT = "insert into users (first_name, last_name , email, password) " +
            "values (?, ?, ?, ?)";
    private static final String DELETE = "delete from users where id = ?";
    private static final String SELECT = "select * from users;";
    private static final String UPDATE = "update users set first_name = ?, last_name = ?, password = ? " +
            " where id=?";
    private static final String FIND_ONE = "SELECT * from users where id = ?";
    private static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String SECRET_KEY = "SSH";


    public UserDatabaseRepository(Validator<User> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    @Override
    public User save(User user) throws IllegalArgumentException, IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAccount().getEmail());
            statement.setString(4, encrypt(user.getAccount().getPassword(),
                    SECRET_KEY));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.save(user);
    }

    @Override
    public User delete(Long id) throws IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.delete(id);
    }

    @Override
    public User findOne(Long userID) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE)) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getUser(userID, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findOne(userID);
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong(ID);
                User user = getUser(id, resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findAll();
    }

    @Override
    public User update(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, encrypt(user.getAccount().getPassword(), SECRET_KEY));
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.update(user);
    }

    private User getUser(Long userID, ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString(FIRST_NAME);
        String lastName = resultSet.getString(LAST_NAME);
        String email = resultSet.getString(EMAIL);
        String password = resultSet.getString(PASSWORD);
        User user = new User(firstName, lastName, new Account(email, password));
        user.setId(userID);
        return user;
    }
}
