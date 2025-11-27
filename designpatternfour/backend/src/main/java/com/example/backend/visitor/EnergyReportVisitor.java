package com.example.backend.visitor;

import com.example.backend.device.AC;
import com.example.backend.device.Door;
import com.example.backend.device.Light;

public class EnergyReportVisitor implements DeviceVisitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visitLight(Light light) {
        String log = "[VISITOR] Energy check LIGHT (" + light.getName() + ") → " +
                (light.isOn() ? "10W" : "0W");
        System.out.println(log);
        report.append(log).append("\n");
    }

    @Override
    public void visitAC(AC ac) {
        String log = "[VISITOR] Energy check AC (" + ac.getName() + ") → " +
                (ac.isOn() ? "1200W" : "0W");
        System.out.println(log);
        report.append(log).append("\n");
    }

    @Override
    public void visitDoor(Door door) {
        String log = "[VISITOR] Energy check DOOR (" + door.getName() + ") → 0W";
        System.out.println(log);
        report.append(log).append("\n");
    }

    public String getReport() {
        return report.toString();
    }
}
