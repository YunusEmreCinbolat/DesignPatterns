package com.example.backend.service.device.state;

import com.example.backend.service.device.Device;

public interface State {
    void turnOn(Device device);
    void turnOff(Device device);
}
