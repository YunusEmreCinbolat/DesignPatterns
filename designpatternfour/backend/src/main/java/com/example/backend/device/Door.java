package com.example.backend.device;


import com.example.backend.memento.DeviceSnapshot;
import com.example.backend.visitor.DeviceVisitor;

public class Door extends Device {

    private boolean open = false;   // Door uses “open/close” but internally → on/off aynı mantık

    public Door(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.DOOR;
    }

    @Override
    public boolean isOn() {
        return open;
    }

    @Override
    public void turnOn() {
        System.out.println("Door opened");
        open = true;
    }

    @Override
    public void turnOff() {
        System.out.println("Door closed");
        open = false;
    }

    @Override
    public DeviceSnapshot createSnapshot() {
        return new DeviceSnapshot(name, open);
    }

    @Override
    public void restore(DeviceSnapshot snapshot) {
        this.open = snapshot.isOn();
    }

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visitDoor(this);
    }
}
