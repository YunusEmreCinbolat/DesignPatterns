package com.example.backend.memento;


public class DeviceSnapshot {
    private final String deviceName;
    private final boolean on;

    public DeviceSnapshot(String deviceName, boolean on) {
        this.deviceName = deviceName;
        this.on = on;
    }

    public String getDeviceName() { return deviceName; }
    public boolean isOn() { return on; }
}
