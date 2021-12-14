package com.example.social_network_bastille.utils;

import java.util.UUID;

public class IdGenerator {
    public static Long generateID() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
