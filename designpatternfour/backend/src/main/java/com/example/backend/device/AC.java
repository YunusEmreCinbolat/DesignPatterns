package com.example.backend.device;

import com.example.backend.device.state.OnState;
import com.example.backend.device.state.OffState;
import com.example.backend.device.state.State;
import com.example.backend.memento.DeviceSnapshot;
import com.example.backend.visitor.DeviceVisitor;

public class AC extends Device {

    private State state = new OffState();
    private boolean on = false;

    public AC(String name) {
        super(name);
    }

    @Override
    public String getName() { return name; }

    @Override
    public DeviceType getType() { return DeviceType.AC; }

    @Override
    public boolean isOn() { return state.isOn(); }

    @Override
    public void turnOn() {
        state = new OnState();
        state.printStateChange(name);
        on = true;
        System.out.println("AC turned ON");
    }

    @Override
    public void turnOff() {
        state = new OffState();
        state.printStateChange(name);
        on = false;
        System.out.println("AC turned OFF");
    }

    @Override
    public DeviceSnapshot createSnapshot() {
        return new DeviceSnapshot(name, state.isOn());
    }

    @Override
    public void restore(DeviceSnapshot snapshot) {
        if (snapshot.isOn()) state = new OnState();
        else state = new OffState();
        this.on = snapshot.isOn();
    }

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visitAC(this);
    }
}
