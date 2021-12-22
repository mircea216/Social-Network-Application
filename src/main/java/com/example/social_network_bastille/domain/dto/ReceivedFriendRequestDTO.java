package com.example.social_network_bastille.domain.dto;

import javafx.scene.control.Button;

public class ReceivedFriendRequestDTO {
    private String from;
    private String date;
    private Button confirm;
    private Button cancel;
    private Long firstID;

    public ReceivedFriendRequestDTO(String from, String date, Button confirm, Button cancel, Long firstID) {
        this.from = from;
        this.date = date;
        this.confirm = confirm;
        this.cancel = cancel;
        this.firstID = firstID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Button getConfirm() {
        return confirm;
    }

    public void setConfirm(Button confirm) {
        this.confirm = confirm;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public Long getFirstID() {
        return firstID;
    }

    public void setFirstID(Long firstID) {
        this.firstID = firstID;
    }
}
