package com.manas.urlshortener.util;

import java.security.MessageDigest;

public class SlugGenerator {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int SLUG_LENGTH = 7;

    private SlugGenerator() {
        // Prevent object creation (utility class)
    }

    public static String generateSlug(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(longUrl.getBytes());

            long num = 0;
            for (int i = 0; i < 8; i++) {
                num = (num << 8) | (hash[i] & 0xff);
            }

            StringBuilder slug = new StringBuilder();
            while (slug.length() < SLUG_LENGTH) {
                int index = (int) Math.floorMod(num, 62);
                slug.append(BASE62.charAt(index));
                num /= 62;
            }

            return slug.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating slug", e);
        }
    }
}
