package com.example.backend.service.device.decorator;

import com.example.backend.service.device.Device;
import com.example.backend.service.device.state.State;

public abstract class DeviceDecorator implements Device {
    protected Device decoratedDevice;

    public DeviceDecorator(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Decorated device cannot be null!");
        }
        this.decoratedDevice = device;
    }

    @Override
    public void turnOn() {
        decoratedDevice.turnOn();
    }

    @Override
    public void turnOff() {
        decoratedDevice.turnOff();
    }

    @Override
    public String getName() {
        return decoratedDevice.getName();
    }

    @Override
    public void setState(State state) {
        decoratedDevice.setState(state);
    }

    @Override
    public State getState() {
        return decoratedDevice.getState();
    }

    /**
     * Frontend'e hangi dekoratörlerin uygulandığını zincirli şekilde döndürür.
     */
    @Override
    public String getDecorators() {
        return decoratedDevice.getDecorators() + " + " + this.getClass().getSimpleName();
    }
}
