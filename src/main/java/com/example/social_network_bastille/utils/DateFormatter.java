package com.example.social_network_bastille.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String FORMAT = "yyyy-MM-dd";
    private static final String FORMATTER = "yyyy-MM-dd HH:mm";

    public static LocalDate geFormattedStringDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        return LocalDate.parse(date, formatter);
    }

    public static String getFormattedLocalDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        return date.format(formatter);
    }

    public static String getFormattedLocalDateTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(FORMATTER));
    }

    public static LocalDateTime getFormattedStringLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        return LocalDateTime.parse(date, formatter);
    }
}

