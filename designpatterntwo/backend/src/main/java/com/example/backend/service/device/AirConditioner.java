package com.example.backend.service.device;

import com.example.backend.service.device.state.OffState;
import com.example.backend.service.device.state.State;

public class AirConditioner implements Device {
    private final String name = "AirConditioner";
    private State state = new OffState();

    @Override
    public void turnOn() {
        if (state == null) state = new OffState();
        state.turnOn(this);
    }

    @Override
    public void turnOff() {
        if (state == null) state = new OffState();
        state.turnOff(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }
}
