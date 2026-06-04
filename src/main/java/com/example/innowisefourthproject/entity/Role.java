package com.example.innowisefourthproject.entity;

public enum Role {
    ADMIN,
    USER;

    public static Role from(String value) {
        if (value == null) {
            return USER;
        }
        return Role.valueOf(value.toUpperCase());
    }
}
