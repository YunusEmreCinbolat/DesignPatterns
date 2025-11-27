package com.example.backend.device;

import com.example.backend.memento.DeviceSnapshot;
import com.example.backend.visitor.DeviceVisitor;

public class Light extends Device {

    private boolean on = false;

    public Light(String name) {
        super(name);
    }

    @Override
    public String getName() { return name; }

    @Override
    public DeviceType getType() { return DeviceType.LIGHT; }

    @Override
    public boolean isOn() { return on; }

    @Override
    public void turnOn() { on = true; }

    @Override
    public void turnOff() { on = false; }

    @Override
    public DeviceSnapshot createSnapshot() {
        return new DeviceSnapshot(name, on);
    }

    @Override
    public void restore(DeviceSnapshot snapshot) {
        this.on = snapshot.isOn();
    }

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visitLight(this);
    }
}
