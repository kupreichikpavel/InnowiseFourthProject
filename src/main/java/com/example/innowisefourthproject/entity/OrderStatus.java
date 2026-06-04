package com.example.innowisefourthproject.entity;

public enum OrderStatus {
    CREATED,
    COMPLETED,
    CANCELLED;

    public static OrderStatus from(String value) {
        if (value == null) {
            return CREATED;
        }

        return OrderStatus.valueOf(value.toUpperCase());
    }
}
