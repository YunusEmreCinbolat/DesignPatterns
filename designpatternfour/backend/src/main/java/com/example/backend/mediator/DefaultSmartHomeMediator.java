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
        System.out.println("[MEDIATOR] Registering device → " + device.getName() +
                " (" + device.getType() + ")");
        devices.put(device.getName(), device);
    }

    @Override
    public Device getDevice(String name) {
        System.out.println("[MEDIATOR] Fetching device → " + name);
        return devices.get(name);
    }

    @Override
    public String sendCommand(DeviceType type, boolean turnOn) {

        StringBuilder result = new StringBuilder();

        devices.values().stream()
                .filter(d -> d.getType() == type)
                .forEach(device -> {
                    Command cmd = turnOn
                            ? new TurnOnCommand(device)
                            : new TurnOffCommand(device);

                    System.out.println("[MEDIATOR] Executing command → " + cmd.getClass().getSimpleName());

                    cmd.execute();

                    String line = device.getName()
                            + " -> "
                            + (turnOn ? "ON" : "OFF");

                    System.out.println("[MEDIATOR] RESULT → " + line);  // 🔥 EKLENDİ

                    result.append(line).append("\n");
                });

        String finalText = result.length() > 0
                ? result.toString()
                : "No device matched type: " + type;

        System.out.println("[MEDIATOR] Final Output →\n" + finalText);  // 🔥 EKLENDİ

        return finalText;
    }

}
