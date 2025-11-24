package com.example.backend.controller;

import com.example.backend.device.Device;
import com.example.backend.visitor.DeviceVisitor;
import com.example.backend.visitor.EnergyReportVisitor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final List<Device> devices;

    public ReportController(List<Device> devices) {
        this.devices = devices;
    }

    @GetMapping("/energy")
    public ResponseEntity<?> energy() {
        DeviceVisitor visitor = new EnergyReportVisitor();
        devices.forEach(d -> d.accept(visitor));
        return ResponseEntity.ok("Energy report generated");
    }
}
