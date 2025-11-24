package com.example.backend.controller;

import com.example.backend.interpreter.Context;
import com.example.backend.interpreter.RuleInterpreterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin(origins = "http://localhost:4200")
public class RuleController {

    private final RuleInterpreterService interpreter;

    public RuleController(RuleInterpreterService interpreter) {
        this.interpreter = interpreter;
    }

    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody RuleRequest request) {

        Context ctx = new Context(28, true); // demo için sabit sensör verisi
        interpreter.evaluateAndExecute(request, ctx);

        return ResponseEntity.ok("Rule executed");
    }
}
