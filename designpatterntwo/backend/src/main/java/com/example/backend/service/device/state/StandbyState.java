package com.example.backend.service.device.state;


import com.example.backend.service.device.Device;

public class StandbyState implements State {
    @Override
    public void turnOn(Device device) {
        device.setState(new OnState());
        System.out.println(device.getName() + " resumed from STANDBY");
    }

    @Override
    public void turnOff(Device device) {
        device.setState(new OffState());
        System.out.println(device.getName() + " turned OFF from STANDBY");
    }
}
