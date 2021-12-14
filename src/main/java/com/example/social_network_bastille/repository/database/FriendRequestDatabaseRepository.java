package com.example.social_network_bastille.repository.database;

import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDateTime;
import static com.example.social_network_bastille.utils.DateFormatter.getFormattedStringLocalDateTime;

public class FriendRequestDatabaseRepository extends AbstractDatabaseRepository<Tuple<Long, Long>, FriendRequest> {
    private static final String APPROVED = "approved";
    private static final String REJECTED = "rejected";
    private static final String PENDING = "pending";
    private static final String INSERT = "insert into friend_requests " +
            "(id_1, id_2, status, date) values (?,?,?,?);";
    private static final String ERROR = "Impossible to send friend request between nonexistent users";
    private static final String DELETE = "delete from friend_requests where id_1= ? and id_2 = ?;";
    private static final String SELECT = "select * from friend_requests";
    private static final String ID_FROM = "id_1";
    private static final String ID_TO = "id_2";
    private static final String STATUS = "status";
    private static final String FIND_ONE = "select * from friend_requests where id_1= ? and id_2 = ?;";
    private static final String EXISTENT_FRIENDSHIP = "Already existent friendship";
    public static final String DATE = "date";
    public static final String UPDATE = "update friend_requests set status = ? " +
            "where id_1= ? and id_2 = ?;";
    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public FriendRequestDatabaseRepository(Validator<FriendRequest> validator, String url, String username,
                                           String password, Repository<Long, User> userRepository,
                                           Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        super(validator, url, username, password);
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) throws IllegalArgumentException,
            IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            repositoryValidation(friendRequest);
            setAttributesForFriendRequest(friendRequest, statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return super.save(friendRequest);
    }

    @Override
    public FriendRequest delete(Tuple<Long, Long> id) throws IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id.getId1());
            statement.setLong(2, id.getId2());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.delete(id);
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                friendRequests.add(getFriendRequest(resultSet));
            }
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findAll();
    }

    @Override
    public FriendRequest findOne(Tuple<Long, Long> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE)) {
            statement.setLong(1, id.getId1());
            statement.setLong(2, id.getId2());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getFriendRequest(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findOne(id);
    }

    @Override
    public FriendRequest update(FriendRequest friendRequest) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            if (findOne(new Tuple<>(friendRequest.getFromUser().getId(), friendRequest.getToUser().getId())) != null) {
                if (findOne(new Tuple<>(friendRequest.getFromUser().getId(),
                        friendRequest.getToUser().getId())).getStatus().equals(Status.PENDING)) {
                    updatedEntity(friendRequest, statement);
                }
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.update(friendRequest);
    }

    private FriendRequest getFriendRequest(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong(ID_FROM);
        Long id2 = resultSet.getLong(ID_TO);
        LocalDateTime date = getFormattedStringLocalDateTime(resultSet.getString(DATE));
        String status = resultSet.getString(STATUS);
        Status requestStatus = getRequestStatus(status);
        FriendRequest friendRequest = new FriendRequest(userRepository.findOne(id1),
                userRepository.findOne(id2), date);
        friendRequest.setStatus(requestStatus);
        return friendRequest;
    }

    private Status getRequestStatus(String status) {
        Status requestStatus = Status.PENDING;
        switch (status) {
            case APPROVED -> requestStatus = Status.APPROVED;
            case PENDING -> {
            }
            case REJECTED -> requestStatus = Status.REJECTED;
        }
        return requestStatus;
    }

    private void setAttributesForFriendRequest(FriendRequest friendRequest, PreparedStatement statement)
            throws SQLException, IllegalFriendshipException {
        if (friendRequest.getStatus() != Status.REJECTED) {
            statement.setLong(1, friendRequest.getFromUser().getId());
            statement.setLong(2, friendRequest.getToUser().getId());
            statement.setString(4, getFormattedLocalDateTime(friendRequest.getDate()));
            Status status = friendRequest.getStatus();
            switch (status) {
                case APPROVED -> {
                    statement.setString(3, APPROVED);
                    friendshipRepository.save(new Friendship(friendRequest.getFromUser().getId(),
                            friendRequest.getToUser().getId(), LocalDate.now()));
                }
                case PENDING -> statement.setString(3, PENDING);
            }
            statement.executeUpdate();
        }
    }

    private void repositoryValidation(FriendRequest friendRequest) throws IllegalFriendshipException {
        if (userRepository.findOne(friendRequest.getFromUser().getId()) == null ||
                userRepository.findOne(friendRequest.getFromUser().getId()) == null) {
            throw new IllegalFriendshipException(ERROR);
        }
        if (friendshipRepository.findOne(new Tuple<>(friendRequest.getFromUser().getId(),
                friendRequest.getToUser().getId())) != null)
            throw new IllegalFriendshipException(EXISTENT_FRIENDSHIP);
    }

    private void updatedEntity(FriendRequest friendRequest, PreparedStatement statement) throws SQLException {
        switch (friendRequest.getStatus()) {
            case APPROVED -> {
                statement.setString(1, APPROVED);
                try {
                    friendshipRepository.save(new Friendship(friendRequest.getFromUser().getId(),
                            friendRequest.getToUser().getId(), LocalDate.now()));
                } catch (IllegalFriendshipException e) {
                    System.out.println(e.getMessage());
                }
            }
            case REJECTED -> {
                statement.setString(1, REJECTED);
                try {
                    this.delete(new Tuple<>(friendRequest.getFromUser().getId(),
                            friendRequest.getToUser().getId()));
                } catch (IllegalFriendshipException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        statement.setLong(2, friendRequest.getFromUser().getId());
        statement.setLong(3, friendRequest.getToUser().getId());
        statement.executeUpdate();
    }
}
