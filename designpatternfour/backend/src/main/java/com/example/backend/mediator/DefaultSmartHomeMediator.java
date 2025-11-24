package com.example.backend.mediator;

import com.example.backend.command.Command;
import com.example.backend.command.TurnOffCommand;
import com.example.backend.command.TurnOnCommand;
import com.example.backend.device.Device;
import com.example.backend.device.DeviceType;

import java.util.HashMap;
import java.util.Map;

public class DefaultSmartHomeMediator implements SmartHomeMediator {

    private final Map<String, Device> devices = new HashMap<>();

    @Override
    public void registerDevice(Device device) {
        devices.put(device.getName(), device);
    }

    @Override
    public Device getDevice(String name) {
        return devices.get(name);
    }

    @Override
    public void sendCommand(DeviceType type, boolean turnOn) {
        devices.values().stream()
                .filter(d -> d.getType() == type)
                .forEach(device -> {
                    Command cmd = turnOn
                            ? new TurnOnCommand(device)
                            : new TurnOffCommand(device);
                    cmd.execute();
                });
    }
}
