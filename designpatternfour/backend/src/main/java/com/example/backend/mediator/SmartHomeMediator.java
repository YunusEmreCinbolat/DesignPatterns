package com.example.backend.mediator;


import com.example.backend.device.Device;
import com.example.backend.device.DeviceType;

public interface SmartHomeMediator {
    void registerDevice(Device device);
    Device getDevice(String name);
    String sendCommand(DeviceType type, boolean turnOn);
}
