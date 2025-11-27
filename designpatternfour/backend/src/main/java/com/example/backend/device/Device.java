package com.example.backend.device;

import com.example.backend.device.state.State;
import com.example.backend.memento.DeviceSnapshot;
import com.example.backend.visitor.DeviceVisitor;

public abstract class Device {

    protected String name;
    protected State state;     // State Pattern
    protected boolean on;      // FE uyumluluğu için

    protected Device(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public abstract DeviceType getType();

    public boolean isOn() {
        return state != null && state.isOn();
    }

    public abstract void turnOn();
    public abstract void turnOff();

    // ---------- MEMENTO ----------
    public DeviceSnapshot createSnapshot() {
        System.out.println("[MEMENTO] Creating snapshot for " + name);

        return new DeviceSnapshot(name, this.isOn());
    }

    public void restore(DeviceSnapshot snapshot) {
        System.out.println("[MEMENTO] Restoring snapshot for " + name +
                " → " + (snapshot.isOn() ? "ON" : "OFF"));
        this.on = snapshot.isOn();
    }

    // ---------- VISITOR ----------
    public abstract void accept(DeviceVisitor visitor);
}
