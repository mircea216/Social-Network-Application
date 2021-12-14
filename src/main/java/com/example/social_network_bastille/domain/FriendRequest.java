package com.example.social_network_bastille.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Tuple<Long, Long>> {
    private static final String SPACE = " ";
    private User fromUser;
    private User toUser;
    private Status status;
    private LocalDateTime date;

    public FriendRequest(User firstUser, User secondUser, LocalDateTime date) {
        this.fromUser = firstUser;
        this.toUser = secondUser;
        setId(new Tuple<>(fromUser.getId(), toUser.getId()));
        this.status = Status.PENDING;
        this.date = date;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequest that)) return false;
        return Objects.equals(fromUser, that.fromUser) && Objects.equals(toUser, that.toUser) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, toUser, status);
    }

    @Override
    public String toString() {
        if (fromUser == null || toUser == null) {
            return "no request";
        }
        return switch (status) {
            case APPROVED -> toUser.getFirstName() + SPACE + toUser.getLastName()
                    + " accepted your friend request";
            case REJECTED -> toUser.getFirstName() + SPACE + toUser.getLastName() +
                    " didn't accept your friend request";
            case PENDING -> "Your request for " +
                    toUser.getFirstName() + SPACE + toUser.getLastName() +
                    " is still pending";
        };
    }
}
