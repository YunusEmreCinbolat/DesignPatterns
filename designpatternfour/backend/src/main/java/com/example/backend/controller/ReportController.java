package com.example.backend.controller;

import com.example.backend.device.Device;
import com.example.backend.visitor.DeviceVisitor;
import com.example.backend.visitor.EnergyReportVisitor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final List<Device> devices;

    public ReportController(List<Device> devices) {
        this.devices = devices;
    }

    @GetMapping("/energy")
    public ResponseEntity<?> energy() {

        EnergyReportVisitor visitor = new EnergyReportVisitor();
        devices.forEach(d -> d.accept(visitor));
        System.out.println("[VISITOR] Energy report generated");

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "report", visitor.getReport()
                )
        );
    }

}
