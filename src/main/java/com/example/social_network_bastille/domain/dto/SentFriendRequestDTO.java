package com.example.social_network_bastille.domain.dto;

import javafx.scene.control.Button;

public class SentFriendRequestDTO {
    private String toWhom;
    private String date;
    private Button cancel;
    private Long toWhomID;

    public SentFriendRequestDTO(String byWhom, String date, Button cancel, Long toWhom) {
        this.toWhom = byWhom;
        this.date = date;
        this.cancel = cancel;
        this.toWhomID = toWhom;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public Long getToWhomID() {
        return toWhomID;
    }

    public void setToWhomID(Long toWhomID) {
        this.toWhomID = toWhomID;
    }
}
