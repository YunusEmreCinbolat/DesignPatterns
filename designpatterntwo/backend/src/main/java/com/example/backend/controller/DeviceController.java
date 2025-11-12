package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import com.example.backend.model.DeviceType;
import com.example.backend.service.command.*;
import com.example.backend.service.device.*;
import com.example.backend.service.device.decorator.*;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = "*")
public class DeviceController {

    private final Invoker invoker = new Invoker();

    @PostMapping("/{type}/on")
    public ResponseEntity<Map<String, String>> turnOn(@PathVariable String type) {
        DeviceType deviceType = DeviceType.fromString(type);
        Device device = createDevice(deviceType);
        Command cmd = new TurnOnCommand(device);

        invoker.executeCommand(cmd);

        return ResponseEntity.ok(
                Map.of(
                        "message", device.getName() + " turned ON",
                        "status", "success",
                        "decorators", device.getDecorators()
                )
        );
    }

    @PostMapping("/{type}/off")
    public ResponseEntity<Map<String, String>> turnOff(@PathVariable String type) {
        DeviceType deviceType = DeviceType.fromString(type);
        Device device = createDevice(deviceType);
        Command cmd = new TurnOffCommand(device);

        invoker.executeCommand(cmd);

        return ResponseEntity.ok(
                Map.of(
                        "message", device.getName() + " turned OFF",
                        "status", "success",
                        "decorators", device.getDecorators()
                )
        );
    }

    private Device createDevice(DeviceType type) {
        switch (type) {
            case LIGHT:
                return new TimerDecorator(new Light());
            case AC:
                return new EnergySaverDecorator(new AirConditioner());
            case TV:
                return new TimerDecorator(new EnergySaverDecorator(new Television()));
            default:
                throw new IllegalArgumentException("Unsupported device type: " + type);
        }
    }
}
