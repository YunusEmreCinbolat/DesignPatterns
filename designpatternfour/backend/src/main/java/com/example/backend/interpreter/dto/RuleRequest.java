package com.example.backend.interpreter.dto;

public record RuleRequest(
        String sensor,
        String operator,
        int value,
        String action,
        String deviceType
) {}
