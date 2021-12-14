package com.example.social_network_bastille.repository.database;

import com.example.social_network_bastille.domain.Entity;
import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDateTime;
import static com.example.social_network_bastille.utils.DateFormatter.getFormattedStringLocalDateTime;


public class MessageDatabaseRepository extends AbstractDatabaseRepository<Long, Message> {
    private static final String ERROR = "NO USER WITH THIS ID";
    private static final String INSERT = "insert into messages (from_user_id, message, date) values (?, ?, ?);";
    private static final String SELECT = "select * from messages;";
    private static final String DELETE = "delete from messages where id = ?;";
    private static final String ID = "id";
    private static final String USER_ID = "from_user_id";
    private static final String DATE = "date";
    private static final String MESSAGE = "message";
    private static final String INSERT_USERS_LIST = "insert into messages_links  (id_to, id_message) values (?, ?);";
    private static final String LAST_MESSAGE = "select id from messages order by id desc limit 1";
    private static final String TO_WHOM = "select id_to from " +
            "messages_links where id_message = ?";
    private static final String FIND_ONE = "select * from messages where id = ?;";
    private static final String ID_TO = "id_to";
    public static final String DELETE_MESSAGES_LINKS = "delete from messages_links " +
            "where id_message= ? ;";
    private final Repository<Long, User> userRepository;

    public MessageDatabaseRepository(Validator<Message> validator, String url, String username, String password, Repository<Long, User> userRepository) {
        super(validator, url, username, password);
        this.userRepository = userRepository;
    }

    @Override
    public Message save(Message message) throws IllegalArgumentException, IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (userRepository.findOne(message.getFrom().getId()) == null)
                throw new Exception(ERROR);
            saveMessageStatement(message, statement);
            PreparedStatement retrieval = connection.prepareStatement(LAST_MESSAGE);
            ResultSet resultSet = retrieval.executeQuery();
            if (resultSet.next()) {
                for (Long id : getToFriendsIDs(message)) {
                    insertMessagesToFriends(connection, resultSet, id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.save(message);
    }

    @Override
    public Message delete(Long id) throws IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            deleteMessagesLinks(id, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.delete(id);
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong(ID);
                Long fromUserID = resultSet.getLong(USER_ID);
                String messageText = resultSet.getString(MESSAGE);
                LocalDateTime date = getFormattedStringLocalDateTime(resultSet.getString(DATE));
                PreparedStatement retrievalOfIDs = connection.prepareStatement(TO_WHOM);
                retrievalOfIDs.setLong(1, id);
                ResultSet set = retrievalOfIDs.executeQuery();
                List<User> listOfUsers = getUsersToWhomMessagesHaveBeenSent(set);
                Message message = new Message(userRepository.findOne(fromUserID), listOfUsers, messageText, date);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findAll();
    }

    @Override
    public Message findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Message message = getMessageFromDatabase(id, connection, resultSet);
                message.setId(id);
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findOne(id);
    }

    private Message getMessageFromDatabase(Long id, Connection connection, ResultSet resultSet) throws SQLException {
        Long fromUserID = resultSet.getLong(USER_ID);
        String messageText = resultSet.getString(MESSAGE);
        LocalDateTime date = getFormattedStringLocalDateTime(resultSet.getString(DATE));
        PreparedStatement retrievalOfIDs = connection.prepareStatement(TO_WHOM);
        retrievalOfIDs.setLong(1, id);
        ResultSet set = retrievalOfIDs.executeQuery();
        List<User> listOfUsers = getUsersToWhomMessagesHaveBeenSent(set);
        return new Message(userRepository.findOne(fromUserID), listOfUsers, messageText,
                date);
    }


    private List<User> getUsersToWhomMessagesHaveBeenSent(ResultSet set) throws SQLException {
        List<User> listOfUsers = new ArrayList<>();
        while (set.next()) {
            Long toWhom = set.getLong(ID_TO);
            listOfUsers.add(userRepository.findOne(toWhom));
        }
        return listOfUsers;
    }

    private void saveMessageStatement(Message message, PreparedStatement statement) throws SQLException {
        statement.setLong(1, message.getFrom().getId());
        statement.setString(2, message.getMessage());
        statement.setString(3, getFormattedLocalDateTime(message.getDate()));
        statement.executeUpdate();
    }

    private List<Long> getToFriendsIDs(Message message) {
        return message.getTo()
                .stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
    }

    private void insertMessagesToFriends(Connection connection, ResultSet resultSet, Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_LIST);
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, resultSet.getLong(ID));
        preparedStatement.executeUpdate();
    }

    private void deleteMessagesLinks(Long id, Connection connection) throws SQLException {
        PreparedStatement deleteFromMessagesLinks = connection.prepareStatement(DELETE_MESSAGES_LINKS);
        deleteFromMessagesLinks.setLong(1, id);
        deleteFromMessagesLinks.executeUpdate();
    }
}
