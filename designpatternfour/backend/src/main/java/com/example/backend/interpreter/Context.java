package com.example.backend.interpreter;


public class Context {
    private final int temperature;
    private final boolean motionDetected;

    public Context(int temperature, boolean motionDetected) {
        this.temperature = temperature;
        this.motionDetected = motionDetected;
    }

    public int getTemperature() { return temperature; }
    public boolean isMotionDetected() { return motionDetected; }
}
