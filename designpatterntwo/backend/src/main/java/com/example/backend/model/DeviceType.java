package com.example.backend.model;

public enum DeviceType {
    LIGHT,
    AC,
    TV;

    public static DeviceType fromString(String value) {
        try {
            return DeviceType.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid device type: " + value);
        }
    }
}
