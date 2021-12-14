package com.example.social_network_bastille.userinterface;

import com.example.social_network_bastille.controller.*;
import com.example.social_network_bastille.domain.*;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.utils.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.example.social_network_bastille.utils.DateFormatter.geFormattedStringDate;
import static com.example.social_network_bastille.utils.DateFormatter.getFormattedStringLocalDateTime;

public class UserInterface {
    private static final String OPTION1 = "1";
    private static final String OPTION2 = "2";
    private static final String OPTION3 = "3";
    private static final String OPTION4 = "4";
    private static final String OPTION5 = "5";
    private static final String OPTION6 = "6";
    private static final String OPTION7 = "7";
    private static final String OPTION8 = "8";
    private static final String OPTION9 = "9";
    private static final String OPTION10 = "10";
    private static final String OPTION11 = "11";
    private static final String OPTION12 = "12";
    private static final String OPTION13 = "13";
    private static final String OPTION14 = "14";
    private static final String OPTION15 = "15";
    private static final String OPTION16 = "16";
    private static final String OPTION17 = "17";
    private static final String OPTION18 = "18";
    private static final String OPTION19 = "19";
    private static final String OPTION20 = "20";
    private static final String OPTION21 = "21";
    private static final String OPTION22 = "22";
    private static final String OPTION23 = "23";
    private static final String OPTION24 = "24";
    private static final String OPTION25 = "25";
    private static final String OPTION26 = "26";
    private static final String EXIT = "x";
    private static final String APPROVED = "approved";
    private static final String REJECTED = "rejected";

    private final UserControllerInterface userController;
    private final FriendshipControllerInterface friendshipController;
    private final MessageControllerInterface messageController;
    private final ReplyMessageControllerInterface replyMessageController;
    private final FriendRequestControllerInterface friendRequestController;

    public UserInterface(UserControllerInterface userController, FriendshipControllerInterface friendshipController, MessageControllerInterface messageController, ReplyMessageControllerInterface replyMessageControllerInterface, FriendRequestControllerInterface friendRequestController) {
        this.userController = userController;
        this.friendshipController = friendshipController;
        this.messageController = messageController;
        this.replyMessageController = replyMessageControllerInterface;
        this.friendRequestController = friendRequestController;
    }

    private static void showMenu() {
        System.out.println("1.  ADD AN USER");
        System.out.println("2.  DELETE AN USER");
        System.out.println("3.  UPDATE AN USER");
        System.out.println("4.  SHOW ALL USERS");
        System.out.println("5.  GET AN USER");
        System.out.println("6.  ADD A FRIENDSHIP");
        System.out.println("7.  DELETE A FRIENDSHIP");
        System.out.println("8.  SHOW ALL FRIENDSHIPS");
        System.out.println("9.  GET A FRIENDSHIP");
        System.out.println("10.  DISPLAY THE NUMBER OF COMMUNITIES OF FRIENDS");
        System.out.println("11. DISPLAY THE MOST NUMEROUS COMMUNITY OF FRIENDS");
        System.out.println("12. DISPLAY THE FRIENDSHIPS OF A GIVEN USER");
        System.out.println("13. DISPLAY THE FRIENDSHIPS OF A GIVEN MONTH");
        System.out.println("14. ADD A MESSAGE");
        System.out.println("15. SHOW ALL MESSAGES");
        System.out.println("16. DELETE A MESSAGE");
        System.out.println("17. GET A MESSAGE");
        System.out.println("18. ADD A REPLY MESSAGE");
        System.out.println("19. SHOW ALL REPLY MESSAGES");
        System.out.println("20. GET A REPLY MESSAGE");
        System.out.println("21. DELETE A REPLY MESSAGE");
        System.out.println("22. ADD A FRIEND REQUEST");
        System.out.println("23. DELETE A FRIEND REQUEST");
        System.out.println("24. SHOW ALL FRIEND REQUESTS");
        System.out.println("25. FIND ONE FRIEND REQUEST");
        System.out.println("26. UPDATE FRIEND REQUEST'S STATUS");
        System.out.println("x. EXIT");
    }

    private void saveUser() {
        System.out.println("Give the first name of the user: ");
        Scanner scannerFirstName = new Scanner(System.in);
        String firstName = scannerFirstName.nextLine();
        System.out.println("Give the last name of the user: ");
        Scanner scannerLastName = new Scanner(System.in);
        String lastName = scannerLastName.nextLine();
        System.out.println("Give the email of the user: ");
        Scanner scannerEmail = new Scanner(System.in);
        String email = scannerEmail.nextLine();
        System.out.println("Give the password of the user: ");
        Scanner scannerPassword = new Scanner(System.in);
        String password = scannerPassword.nextLine();
        try {
            User user = new User(firstName, lastName, new Account(email, password));
            user.setId(IdGenerator.generateID());
            userController.saveUser(user);
        } catch (IllegalArgumentException | IllegalFriendshipException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void updateUser() {
        System.out.println("Give the ID of the user to be updated: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        System.out.println("Give the first name of the user: ");
        Scanner scannerFirstName = new Scanner(System.in);
        String firstName = scannerFirstName.nextLine();
        System.out.println("Give the last name of the user: ");
        Scanner scannerLastName = new Scanner(System.in);
        String lastName = scannerLastName.nextLine();
        System.out.println("Give the email of the user: ");
        Scanner scannerEmail = new Scanner(System.in);
        String email = scannerEmail.nextLine();
        System.out.println("Give the password of the user: ");
        Scanner scannerPassword = new Scanner(System.in);
        String password = scannerPassword.nextLine();
        try {
            User user = new User(firstName, lastName, new Account(email, password));
            user.setId(id);
            userController.updateUser(user);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void deleteUser() {
        System.out.println("Give the ID of the user to be removed: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        try {
            userController.deleteUser(id);
        } catch (IllegalArgumentException | IllegalFriendshipException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void findAllUsers() {
        System.out.println("The users are: ");
        for (Map.Entry<Long, List<Long>> pair : friendshipController.loadUsersAndFriends().entrySet()) {
            System.out.println(userController.getUserByID(pair.getKey()));
            System.out.println("FRIENDS:");
            if (!pair.getValue().isEmpty()) {
                pair.getValue().forEach(ID -> System.out.println(userController.getUserByID(ID)));
            } else {
                System.out.println("NONE");
            }
            System.out.println();
        }
    }

    private void getAnUserByID() {
        System.out.println("Give the ID of the user: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id = scannerID1.nextLong();
        if (userController.getUserByID(id) != null) {
            System.out.println(userController.getUserByID(id));
        } else {
            System.out.println("No user with this ID");
        }
    }

    private void displayTheNumberOfCommunitiesOfFriends() {
        System.out.println("The number of communities is: " + friendshipController.
                countTheNumberOfCommunitiesInTheNetwork());
    }

    public void saveFriendship() {
        System.out.println("Give the ID of the first friend: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second friend: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        System.out.println("Enter the date with the next format yyyy-MM-dd: ");
        Scanner scannerDate = new Scanner(System.in);
        String datetime = scannerDate.nextLine();
        LocalDate date = geFormattedStringDate(datetime);
        Friendship friendship = new Friendship(id1, id2, date);
        try {
            friendshipController.saveFriendship(friendship);
        } catch (IllegalFriendshipException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void deleteFriendship() {
        System.out.println("Give the ID of the first friend: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second friend: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        try {
            friendshipController.deleteFriendship(new Tuple<>(id1, id2));
        } catch (IllegalArgumentException | IllegalFriendshipException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void findAllFriendships() {
        System.out.println("The friendships are: ");
        friendshipController.findAll().forEach(System.out::println);
    }

    private void getAFriendshipByID() {
        System.out.println("Give the ID of the first friend: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second friend: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        if (friendshipController.getFriendshipByID(id) != null) {
            System.out.println(friendshipController.getFriendshipByID(id));
        } else {
            System.out.println("No friendship with this ID");
        }
    }

    private void displayTheMostNumerousCommunityOfFriends() {
        System.out.println("The number of users belonging to the most numerous community of friends is: ");
        System.out.println(friendshipController.getTheMostNumerousCommunityOfFriends().size());
    }

    private void displayFriendshipsOfGivenUser() {
        System.out.println("Give the ID of the user: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        System.out.println("Give the first name: ");
        Scanner scannerFirstName = new Scanner(System.in);
        String firstName = scannerFirstName.nextLine();
        System.out.println("Give the last name: ");
        Scanner scannerLastName = new Scanner(System.in);
        String lastName = scannerLastName.nextLine();
        System.out.println("Give the email of the user: ");
        Scanner scannerEmail = new Scanner(System.in);
        String email = scannerEmail.nextLine();
        System.out.println("Give the password of the user: ");
        Scanner scannerPassword = new Scanner(System.in);
        String password = scannerPassword.nextLine();
        User user = new User(firstName, lastName, new Account(email, password));
        user.setId(id);
        friendshipController.displayFriendshipsOfGivenUser(user).forEach(System.out::println);
    }


    private void displayTheFriendshipsOfGivenMonth() {
        System.out.println("Give the month of the friendships");
        Scanner scannerMonth = new Scanner(System.in);
        int month = scannerMonth.nextInt();
        System.out.println("Give the year of the friendships");
        Scanner scannerYear = new Scanner(System.in);
        int year = scannerYear.nextInt();
        friendshipController.displayFriendshipOfGivenMonth(month, year).forEach(System.out::println);
    }

    private void saveMessage() {
        System.out.println("Give the ID of the user: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        User from = userController.getUserByID(id);
        System.out.println("Give the number of users to send the message: ");
        Scanner scannerNumber = new Scanner(System.in);
        int numberOfUsers = scannerNumber.nextInt();
        List<User> toWhomList = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            System.out.println("Give the ID of the user to send the message: ");
            Scanner scannerIdToWhom = new Scanner(System.in);
            Long idToWhom = scannerIdToWhom.nextLong();
            toWhomList.add(userController.getUserByID(idToWhom));
        }
        System.out.println("Give the message: ");
        Scanner scannerMessage = new Scanner(System.in);
        String message = scannerMessage.nextLine();
        System.out.println("Enter the date of the message with the next format yyyy-MM-dd HH:mm : ");
        Scanner scannerDate = new Scanner(System.in);
        String datetime = scannerDate.nextLine();
        Message message1 = new Message(from, toWhomList, message,
                getFormattedStringLocalDateTime(datetime));
        try {
            messageController.saveMessage(message1);
        } catch (IllegalFriendshipException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMessage() {
        System.out.println("Give the ID of the message to be deleted: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        try {
            messageController.deleteMessage(id);
        } catch (IllegalFriendshipException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findAllMessages() {
        messageController.findAll().forEach(System.out::println);
    }

    private void findMessageByID() {
        System.out.println("Give the ID of the message: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        if (messageController.getMessageByID(id) != null) {
            System.out.println(messageController.getMessageByID(id));
        } else {
            System.out.println("There is no message with this id");
        }
    }

    private void saveReplyMessage() {
        System.out.println("Give the ID of the user that sends the message: ");
        Scanner scannerIdFrom = new Scanner(System.in);
        Long idFrom = scannerIdFrom.nextLong();
        System.out.println("Give the ID of the user to whom the reply needs to be sent: ");
        Scanner scannerIdTo = new Scanner(System.in);
        Long idTo = scannerIdTo.nextLong();
        System.out.println("Give the message: ");
        Scanner scannerMessage = new Scanner(System.in);
        String message = scannerMessage.nextLine();
        System.out.println("Give the message: ");
        Scanner scannerIdMessage = new Scanner(System.in);
        Long idMessage = scannerMessage.nextLong();
        ReplyMessage replyMessage = new ReplyMessage(userController.getUserByID(idFrom),
                List.of(userController.getUserByID(idTo)), message,
                LocalDateTime.now(), messageController.getMessageByID(idMessage));
        try {
            replyMessageController.add(replyMessage);
        } catch (IllegalFriendshipException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findReplyMessageByID() {
        System.out.println("Give the ID of the reply message: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        if (replyMessageController.getReplyMessageByID(id) != null) {
            System.out.println(replyMessageController.getReplyMessageByID(id));
        } else {
            System.out.println("There is no reply message with this id");
        }
    }

    private void findAllReplyMessages() {
        replyMessageController.findAll().forEach(System.out::println);
    }

    private void deleteReplyMessage() {
        System.out.println("Give the ID of the reply to be deleted: ");
        Scanner scannerID = new Scanner(System.in);
        Long id = scannerID.nextLong();
        try {
            replyMessageController.deleteReplyMessage(id);
        } catch (IllegalFriendshipException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveFriendRequest() {
        System.out.println("Give the ID of the first user: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second user: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        System.out.println("Give the status of the friendship");
        Scanner scannerStatus = new Scanner(System.in);
        String status = scannerStatus.nextLine();
        FriendRequest friendRequest = new FriendRequest(userController.getUserByID(id1), userController.getUserByID(id2),
                LocalDateTime.now());
        switch (status) {
            case APPROVED -> friendRequest.setStatus(Status.APPROVED);
            case REJECTED -> friendRequest.setStatus(Status.REJECTED);
        }
        try {
            friendRequestController.saveFriendRequest(friendRequest);
        } catch (IllegalFriendshipException e) {
            e.printStackTrace();
        }
    }

    private void deleteFriendRequest() {
        System.out.println("Give the ID of the first user: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second user: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        try {
            friendRequestController.deleteFriendRequest(new Tuple<>(id1, id2));
        } catch (IllegalArgumentException | IllegalFriendshipException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void findAllFriendRequests() {
        friendRequestController.findAll().forEach(System.out::println);
    }

    private void getAFriendRequestByID() {
        System.out.println("Give the ID of the first user: ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second user: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        if (friendRequestController.getFriendRequestById(id) != null) {
            System.out.println(friendRequestController.getFriendRequestById(id));
        } else {
            System.out.println("No request with this ID");
        }
    }

    private void updateFriendRequestStatus() {
        System.out.println("Give the ID of the first user : ");
        Scanner scannerID1 = new Scanner(System.in);
        Long id1 = scannerID1.nextLong();
        System.out.println("Give the ID of the second user: ");
        Scanner scannerID2 = new Scanner(System.in);
        Long id2 = scannerID2.nextLong();
        System.out.println("Give the status of the friendship");
        Scanner scannerStatus = new Scanner(System.in);
        String status = scannerStatus.nextLine();
        FriendRequest friendRequest = friendRequestController.getFriendRequestById(new Tuple<>(id1, id2));
        if (friendRequest != null) {
            switch (status) {
                case APPROVED -> {
                    friendRequest.setStatus(Status.APPROVED);
                    friendRequestController.updateFriendRequest(friendRequest);
                }
                case REJECTED -> {
                    friendRequest.setStatus(Status.REJECTED);
                    friendRequestController.updateFriendRequest(friendRequest);
                }
            }
        } else {
            System.out.println("A nonexistent cannot be updated");
        }
    }

    public void run() {
        String option;
        label:
        while (true) {
            showMenu();
            System.out.println("Select the option: ");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextLine();
            switch (option) {
                case OPTION1:
                    saveUser();
                    break;
                case OPTION2:
                    deleteUser();
                    break;
                case OPTION3:
                    updateUser();
                    break;
                case OPTION4:
                    findAllUsers();
                    break;
                case OPTION5:
                    getAnUserByID();
                    break;
                case OPTION6:
                    saveFriendship();
                    break;
                case OPTION7:
                    deleteFriendship();
                    break;
                case OPTION8:
                    findAllFriendships();
                    break;
                case OPTION9:
                    getAFriendshipByID();
                    break;
                case OPTION10:
                    displayTheNumberOfCommunitiesOfFriends();
                    break;
                case OPTION11:
                    displayTheMostNumerousCommunityOfFriends();
                    break;
                case OPTION12:
                    displayFriendshipsOfGivenUser();
                    break;
                case OPTION13:
                    displayTheFriendshipsOfGivenMonth();
                    break;
                case OPTION14:
                    saveMessage();
                    break;
                case OPTION15:
                    findAllMessages();
                    break;
                case OPTION16:
                    deleteMessage();
                    break;
                case OPTION17:
                    findMessageByID();
                    break;
                case OPTION18:
                    saveReplyMessage();
                    break;
                case OPTION19:
                    findAllReplyMessages();
                    break;
                case OPTION20:
                    findReplyMessageByID();
                    break;
                case OPTION21:
                    deleteReplyMessage();
                    break;
                case OPTION22:
                    saveFriendRequest();
                    break;
                case OPTION23:
                    deleteFriendRequest();
                    break;
                case OPTION24:
                    findAllFriendRequests();
                    break;
                case OPTION25:
                    getAFriendRequestByID();
                    break;
                case OPTION26:
                    updateFriendRequestStatus();
                    break;
                case EXIT:
                    break label;
                default:
                    System.out.println("The selected option is invalid!");
                    break;
            }
        }
    }
}
