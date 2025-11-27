package com.example.backend.command;

import com.example.backend.device.Device;

public class TurnOffCommand implements Command {

    private final Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        System.out.println("COMMAND → TURN OFF " + device.getName());
        device.turnOff();
    }
}
