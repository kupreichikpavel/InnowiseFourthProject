package com.example.innowisefourthproject.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordEncoder {
    private static final String SHA_256 = "SHA-256";

    private PasswordEncoder() {
    }

    public static String encode(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm was not found", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        if (encodedPassword == null || encodedPassword.isBlank()) {
            return false;
        }

        return encode(rawPassword).equals(encodedPassword);
    }

}