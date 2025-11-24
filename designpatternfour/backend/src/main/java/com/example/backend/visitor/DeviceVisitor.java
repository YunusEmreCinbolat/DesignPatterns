package com.example.backend.visitor;

import com.example.backend.device.AC;
import com.example.backend.device.Door;
import com.example.backend.device.Light;

public interface DeviceVisitor {
    void visitLight(Light light);
    void visitAC(AC ac);
    void visitDoor(Door door);
}
