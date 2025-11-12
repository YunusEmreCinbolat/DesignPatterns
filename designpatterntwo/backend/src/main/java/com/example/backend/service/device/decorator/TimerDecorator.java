package com.example.backend.service.device.decorator;

import com.example.backend.service.device.Device;

public class TimerDecorator extends DeviceDecorator {

    public TimerDecorator(Device device) {
        super(device);
    }

    @Override
    public void turnOn() {
        super.turnOn();
        System.out.println("⏱ Timer started for " + getName() + ": device will auto-off after 5 minutes");
    }

    @Override
    public void turnOff() {
        System.out.println("⏱ Timer stopped for " + getName());
        super.turnOff();
    }
}
