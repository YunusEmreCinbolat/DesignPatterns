package com.example.backend.visitor;

import com.example.backend.device.AC;
import com.example.backend.device.Door;
import com.example.backend.device.Light;

public class EnergyReportVisitor implements DeviceVisitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visitLight(Light light) {

        System.out.println("[VISITOR] Visiting LIGHT device → " + light.getName());
        System.out.println("[VISITOR] Current State → " + (light.isOn() ? "ON" : "OFF"));

        String watt = light.isOn() ? "10W" : "0W";

        System.out.println("[VISITOR] Calculated Energy Usage → " + watt);
        System.out.println("[VISITOR] Adding to report...\n");

        report.append("Light '")
                .append(light.getName())
                .append("' → ")
                .append(watt)
                .append("\n");
    }

    @Override
    public void visitAC(AC ac) {

        System.out.println("[VISITOR] Visiting AC device → " + ac.getName());
        System.out.println("[VISITOR] Current State → " + (ac.isOn() ? "ON" : "OFF"));

        String watt = ac.isOn() ? "1200W" : "0W";

        System.out.println("[VISITOR] Calculated Energy Usage → " + watt);
        System.out.println("[VISITOR] Adding to report...\n");

        report.append("AC '")
                .append(ac.getName())
                .append("' → ")
                .append(watt)
                .append("\n");
    }

    @Override
    public void visitDoor(Door door) {

        System.out.println("[VISITOR] Visiting DOOR device → " + door.getName());
        System.out.println("[VISITOR] Current State → " + (door.isOn() ? "OPEN" : "CLOSED"));

        String watt = "0W"; // kapı enerji harcamaz :)

        System.out.println("[VISITOR] Calculated Energy Usage → " + watt);
        System.out.println("[VISITOR] Adding to report...\n");

        report.append("Door '")
                .append(door.getName())
                .append("' → ")
                .append(watt)
                .append("\n");
    }

    public String getReport() {
        System.out.println("[VISITOR] FINAL ENERGY REPORT CREATED");
        return report.toString();
    }
}
