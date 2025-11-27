package com.example.backend.device.state;

public class OffState implements State {

    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public void printStateChange(String deviceName) {
        System.out.println(deviceName + " switched to OFF state");
    }
}
