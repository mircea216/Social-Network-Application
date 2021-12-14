package com.example.social_network_bastille.domain;

import java.time.LocalDate;

import static com.example.social_network_bastille.utils.DateFormatter.getFormattedLocalDate;


public class Friendship extends Entity<Tuple<Long, Long>> {
    private LocalDate date;

    public Friendship(Long firstID, Long secondID, LocalDate date) {
        Tuple<Long, Long> ID = new Tuple<>(firstID, secondID);
        this.setId(ID);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getId().toString() + " DATE: " + getFormattedLocalDate(date);
    }
}
