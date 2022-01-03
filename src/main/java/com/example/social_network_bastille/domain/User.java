package com.example.social_network_bastille.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private Account account;
    private List<User> listOfFriends;

    public User(String firstName, String lastName, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
        listOfFriends = new ArrayList<>();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<User> getListOfFriends() {
        return listOfFriends;
    }

    public void setListOfFriends(List<User> listOfFriends) {
        this.listOfFriends = listOfFriends;
    }

    public void addFriend(User user) {
        listOfFriends.add(user);
    }

    public void removeFriend(User user) {
        listOfFriends.remove(user);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, listOfFriends);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass())
            return false;
        User user = (User) o;
        return Objects.equals(user.firstName, firstName) && Objects.equals(user.lastName, lastName) && Objects.
                equals(user.getListOfFriends(), listOfFriends);
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " FIRSTNAME: " + firstName + " LASTNAME: " + lastName;
    }

}
