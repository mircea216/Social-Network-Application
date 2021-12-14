package com.example.social_network_bastille.repository.file;

import com.example.social_network_bastille.domain.Friendship;
import com.example.social_network_bastille.domain.Tuple;
import com.example.social_network_bastille.domain.User;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.ValidationException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.Repository;

import java.time.LocalDate;
import java.util.List;


public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {
    private final Repository<Long, User> userRepository;

    public FriendshipFileRepository(String fileName, Validator<Friendship> validator, Repository<Long,
            User> userRepository) throws ValidationException {
        super(fileName, validator);
        this.userRepository = userRepository;
        loadFriendshipsAndUpdateListOfFriends();
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        LocalDate date = LocalDate.parse(attributes.get(2));
        Long firstID = Long.parseLong(attributes.get(0));
        Long secondID = Long.parseLong(attributes.get(1));
        return new Friendship(firstID, secondID, date);
    }

    @Override
    protected String createEntityAsString(Friendship friendship) {
        return friendship.getId().getId1() + ";" + friendship.getId().getId2() + ";" +
                friendship.getDate();
    }

    @Override
    public Friendship save(Friendship friendship) throws IllegalFriendshipException {
        Long firstID = friendship.getId().getId1();
        Long secondID = friendship.getId().getId2();
        if (userRepository.findOne(firstID) == null || userRepository.findOne(secondID) == null) {
            throw new IllegalFriendshipException("There is no user with this ID! The friendship " +
                    "cannot be established!");
        }
        Tuple<Long, Long> complementaryID = new Tuple<>(secondID, firstID);
        if (findOne(complementaryID) != null) {
            throw new IllegalFriendshipException("The friendship already exists!");
        }
        User firstUser = userRepository.findOne(firstID);
        User secondUser = userRepository.findOne(secondID);
        firstUser.addFriend(secondUser);
        secondUser.addFriend(firstUser);
        return super.save(friendship);
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) throws IllegalFriendshipException {
        if (super.findOne(id) != null) {
            User firstUser = userRepository.findOne(id.getId1());
            User secondUser = userRepository.findOne(id.getId2());
            firstUser.removeFriend(secondUser);
            secondUser.removeFriend(firstUser);
            return super.delete(id);
        } else if (super.findOne(new Tuple<>(id.getId2(), id.getId1())) != null) {
            User firstUser = userRepository.findOne(id.getId2());
            User secondUser = userRepository.findOne(id.getId1());
            firstUser.removeFriend(secondUser);
            secondUser.removeFriend(firstUser);
            return super.delete(new Tuple<>(id.getId2(), id.getId1()));
        } else throw new IllegalFriendshipException("There is no friendship with this ID!");
    }

    public void loadFriendshipsAndUpdateListOfFriends() throws ValidationException {
        for (Friendship friendship : super.findAll()) {
            Long firstID = friendship.getId().getId1();
            Long secondID = friendship.getId().getId2();
            User user1 = userRepository.findOne(firstID);
            User user2 = userRepository.findOne(secondID);
            if (user1 != null && user2 != null) {
                user1.addFriend(user2);
                user2.addFriend(user1);
            } else {
                throw new ValidationException("There are no users having one of the ID's of the friendship!");
            }
        }
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        if (super.findOne(id) != null) {
            return super.findOne(id);
        }
        Tuple<Long, Long> complementaryID = new Tuple<>(id.getId2(), id.getId1());
        if (super.findOne(complementaryID) != null) {
            return super.findOne(complementaryID);
        }
        return null;
    }
}
