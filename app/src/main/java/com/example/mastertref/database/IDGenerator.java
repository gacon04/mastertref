package com.example.mastertref.database;

import java.util.Random;

public class IDGenerator {
    private static final String PREFIX = "tref_";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 8;

    public static String generateTrefID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder(PREFIX);

        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            id.append(CHARACTERS.charAt(index));
        }

        return id.toString();
    }
}
