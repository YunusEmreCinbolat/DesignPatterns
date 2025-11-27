package com.example.backend.device.state;

public class OnState implements State {

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public void printStateChange(String deviceName) {
        System.out.println(deviceName + " switched to ON state");
    }
}
