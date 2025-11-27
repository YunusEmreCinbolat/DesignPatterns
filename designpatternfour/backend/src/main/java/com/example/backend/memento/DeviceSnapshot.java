package com.example.backend.memento;

public class DeviceSnapshot {

    private final String name;
    private final boolean on;

    public DeviceSnapshot(String name, boolean on) {
        this.name = name;
        this.on = on;

        System.out.println("[MEMENTO] Snapshot created → " + name + " | state=" + (on ? "ON" : "OFF"));
    }

    public String getName() { return name; }
    public boolean isOn() { return on; }
}
