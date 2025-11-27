package com.example.backend.device.state;

public interface State {
    boolean isOn();
    void printStateChange(String deviceName);
}
