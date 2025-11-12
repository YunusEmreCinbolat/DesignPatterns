package com.example.backend.service.device.state;

import com.example.backend.service.device.Device;

public class OffState implements State {
    @Override
    public void turnOn(Device device) {
        if (device == null) return;
        device.setState(new OnState());
        System.out.println(device.getName() + " turned ON");
    }

    @Override
    public void turnOff(Device device) {
        if (device == null) return;
        System.out.println(device.getName() + " is already OFF");
    }
}
