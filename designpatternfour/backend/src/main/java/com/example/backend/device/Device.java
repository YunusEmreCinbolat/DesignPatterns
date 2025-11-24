package com.example.backend.device;

import com.example.backend.memento.DeviceSnapshot;
import com.example.backend.visitor.DeviceVisitor;

public interface Device {
    String getName();
    DeviceType getType();
    boolean isOn();

    void turnOn();
    void turnOff();

    DeviceSnapshot createSnapshot();
    void restore(DeviceSnapshot snapshot);

    void accept(DeviceVisitor visitor);
}
