package com.example.backend.service.device.decorator;

import com.example.backend.service.device.Device;

public class EnergySaverDecorator extends DeviceDecorator {

    public EnergySaverDecorator(Device device) {
        super(device);
    }

    @Override
    public void turnOn() {
        System.out.println("🔋 Energy saver mode enabled for " + getName());
        super.turnOn();
    }

    @Override
    public void turnOff() {
        System.out.println("🔋 Energy saver data saved for " + getName());
        super.turnOff();
    }
}
