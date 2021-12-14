package com.example.social_network_bastille.repository.database;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<Long, Long>, Friendship> {

    private static final String SELECT = "select * from friendships";
    private static final String INSERT = "insert into friendships (id_user_1,id_user_2,date) values (?,?,?);";
    private static final String DELETE = "delete from friendships where id_user_1 = ? and id_user_2 = ? or" +
            "(id_user_2 = ? and id_user_1 = ?)";
    private static final String FIND_ONE = "select * from friendships where (id_user_1 = ? and id_user_2 = ?) or " +
            "(id_user_2 = ? and id_user_1 = ?)";
    private static final String ID_USER_1 = "id_user_1";
    private static final String ID_USER_2 = "id_user_2";
    private static final String DATE = "date";
    private static final String PRIMARY_KEY_VIOLATION = "Primary key violation! The friendship already exists!";

    private final Repository<Long, User> userRepository;


    public FriendshipDatabaseRepository(Validator<Friendship> validator, String url, String username, String password,
                                        Repository<Long, User> userRepository) {
        super(validator, url, username, password);
        this.userRepository = userRepository;
    }

    @Override
    public Friendship save(Friendship friendship) throws IllegalArgumentException, IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (findOne(new Tuple<>(friendship.getId().getId1(), friendship.getId().getId2())) != null)
                throw new IllegalFriendshipException(PRIMARY_KEY_VIOLATION);
            statement.setLong(1, friendship.getId().getId1());
            statement.setLong(2, friendship.getId().getId2());
            statement.setDate(3, Date.valueOf(friendship.getDate()));
            statement.executeUpdate();
            manageFriendships(friendship);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.save(friendship);
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) throws IllegalFriendshipException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id.getId1());
            statement.setLong(2, id.getId2());
            statement.setLong(3, id.getId1());
            statement.setLong(4, id.getId2());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.delete(id);
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(FIND_ONE)) {
            statement.setLong(1, id.getId1());
            statement.setLong(2, id.getId2());
            statement.setLong(3, id.getId1());
            statement.setLong(4, id.getId2());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getFriendship(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(getFriendship(resultSet));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.findAll();
    }

    private Friendship getFriendship(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong(ID_USER_1);
        Long id2 = resultSet.getLong(ID_USER_2);
        Date date = resultSet.getDate(DATE);
        return new Friendship(id1, id2, date.toLocalDate());
    }

    private void manageFriendships(Friendship friendship) {
        User user1 = userRepository.findOne(friendship.getId().getId1());
        User user2 = userRepository.findOne(friendship.getId().getId2());
        user1.addFriend(user2);
        user2.addFriend(user1);
    }
}
