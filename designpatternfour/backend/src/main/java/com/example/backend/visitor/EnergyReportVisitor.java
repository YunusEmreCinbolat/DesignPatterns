package com.example.backend.visitor;

import com.example.backend.device.AC;
import com.example.backend.device.Door;
import com.example.backend.device.Light;

public class EnergyReportVisitor implements DeviceVisitor {

    @Override
    public void visitLight(Light light) {
        System.out.println("[Energy] Light → " + (light.isOn() ? "10W" : "0W"));
    }

    @Override
    public void visitAC(AC ac) {
        System.out.println("[Energy] AC → " + (ac.isOn() ? "1200W" : "0W"));
    }

    @Override
    public void visitDoor(Door door) {
        System.out.println("[Energy] Door → 0W");
    }
}
