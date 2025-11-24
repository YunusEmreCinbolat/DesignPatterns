package com.example.backend.memento;


import java.util.ArrayDeque;
import java.util.Deque;

public class DeviceHistory {

    private final Deque<DeviceSnapshot> history = new ArrayDeque<>();

    public void push(DeviceSnapshot snapshot) {
        history.push(snapshot);
    }

    public DeviceSnapshot pop() {
        return history.isEmpty() ? null : history.pop();
    }
}
