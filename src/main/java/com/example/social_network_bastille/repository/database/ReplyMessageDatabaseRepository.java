package com.example.social_network_bastille.repository.database;

import com.example.social_network_bastille.domain.Message;
import com.example.social_network_bastille.domain.ReplyMessage;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDateTime;
import static com.example.social_network_bastille.utils.DateFormatter.getFormattedStringLocalDateTime;

public class ReplyMessageDatabaseRepository extends AbstractDatabaseRepository<Long, ReplyMessage> {
    private static final String INSERT = "insert into reply_messages (id_message, "
            + "from_id, date, text_reply, by_whom_id) values (?, ?, ?, ?, ?);";
    private static final String ERROR = "The message cannot be sent by wrong replyMessages!";
    private static final String ID = "id";
    private static final String ID_MESSAGE = "id_message";
    private static final String TEXT_REPLY = "text_reply";
    private static final String FROM_ID = "from_id";
    private static final String BY_WHOM_ID = "by_whom_id";
    private static final String DATE = "date";
    private static final String SELECT = "select * from reply_messages";
    private static final String DELETE = "delete from reply_messages where id = ?";
    public static final String FIND_ONE = "select * from reply_messages where id = ?";
    private final Repository<Long, Message> messageRepository;
    private final Repository<Long, User> userRepository;

    public ReplyMessageDatabaseRepository(Validator<ReplyMessage> validator, String url,
                                          String username, String password, Repository<Long, Message> messageRepository, Repository<Long, User> userRepository) {
        super(validator, url, username, password);
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReplyMessage save(ReplyMessage replyMessage) throws IllegalArgumentException, IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (!getToWhomIds(replyMessage).contains(replyMessage.getFrom().getId())) {
                throw new Exception(ERROR);
            }
            if (replyMessage.getReceivedMessage().getId() != null) {
                setReplyMessageAttributes(replyMessage, statement);
                messageRepository.save(new Message(replyMessage.getFrom(),
                        List.of(replyMessage.getReceivedMessage().getFrom()), replyMessage.getMessage(),
                        LocalDateTime.now()));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.save(replyMessage);
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        Set<ReplyMessage> replyMessages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong(ID);
                Long idMessage = resultSet.getLong(ID_MESSAGE);
                String textMessage = resultSet.getString(TEXT_REPLY);
                Long fromMessageID = resultSet.getLong(FROM_ID);
                Long toReplyID = resultSet.getLong(BY_WHOM_ID);
                LocalDateTime date = getFormattedStringLocalDateTime(resultSet.getString(DATE));
                ReplyMessage replyMessage = getReplyMessage(idMessage, textMessage, fromMessageID, toReplyID, date);
                replyMessage.setId(id);
                replyMessages.add(replyMessage);
            }
            return replyMessages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findAll();
    }

    @Override
    public ReplyMessage delete(Long id) throws IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            ReplyMessage replyMessageToDelete = this.findOne(id);
            for (Message message : this.messageRepository.findAll()) {
                if (replyMessageToDelete.getDate().equals(message.getDate()) &&
                        replyMessageToDelete.getMessage().equals(message.getMessage())
                        && replyMessageToDelete.getFrom().getId().equals(message.getFrom().getId())) {
                    this.messageRepository.delete(message.getId());
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.delete(id);
    }

    @Override
    public ReplyMessage findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getReplyMessage(id, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findOne(id);
    }

    private List<Long> getToWhomIds(ReplyMessage replyMessage) {
        return replyMessage.getReceivedMessage().getTo()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    private void setReplyMessageAttributes(ReplyMessage replyMessage, PreparedStatement statement) throws SQLException {
        statement.setLong(1, replyMessage.getReceivedMessage().getId());
        statement.setLong(2, replyMessage.getReceivedMessage().getFrom().getId());
        statement.setString(3, getFormattedLocalDateTime(LocalDateTime.now()));
        statement.setString(4, replyMessage.getMessage());
        statement.setLong(5, replyMessage.getFrom().getId());
        statement.executeUpdate();
    }

    private ReplyMessage getReplyMessage(Long idMessage, String textMessage, Long fromMessageID, Long toReplyID,
                                         LocalDateTime date) {
        return new ReplyMessage(
                userRepository.findOne(toReplyID),
                List.of(userRepository.findOne(fromMessageID)),
                textMessage,
                date, messageRepository.findOne(idMessage));
    }

    private ReplyMessage getReplyMessage(Long id, ResultSet resultSet) throws SQLException {
        Long idMessage = resultSet.getLong(ID_MESSAGE);
        String textMessage = resultSet.getString(TEXT_REPLY);
        Long fromMessageID = resultSet.getLong(FROM_ID);
        Long toReplyID = resultSet.getLong(BY_WHOM_ID);
        LocalDateTime date = getFormattedStringLocalDateTime(resultSet.getString(DATE));
        ReplyMessage replyMessage = getReplyMessage(idMessage, textMessage, fromMessageID, toReplyID, date);
        replyMessage.setId(id);
        return replyMessage;
    }
}
