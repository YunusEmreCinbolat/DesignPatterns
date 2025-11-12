package com.example.backend.service.device;

import com.example.backend.service.device.state.State;

public interface Device {
    void turnOn();
    void turnOff();
    String getName();
    void setState(State state);
    State getState();

    // Dekoratörler için bilgi
    default String getDecorators() {
        return "";
    }
}
