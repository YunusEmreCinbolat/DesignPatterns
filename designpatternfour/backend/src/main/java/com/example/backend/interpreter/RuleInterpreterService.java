package com.example.backend.interpreter;

import com.example.backend.device.DeviceType;
import com.example.backend.interpreter.dto.RuleRequest;
import com.example.backend.mediator.SmartHomeMediator;
import org.springframework.stereotype.Service;

@Service
public class RuleInterpreterService {

    private final SmartHomeMediator mediator;

    public RuleInterpreterService(SmartHomeMediator mediator) {
        this.mediator = mediator;
    }

    public String evaluateAndExecute(RuleRequest rule, Context ctx) {

        System.out.println("Interpreter: evaluating rule → " + rule);

        boolean condition = false;

        // TEMPERATURE CHECK
        if (rule.sensor().equals("TEMPERATURE")) {
            System.out.println("Checking TEMPERATURE → " + ctx.getTemperature() + " " + rule.operator() + " " + rule.value());

            condition = switch (rule.operator()) {
                case ">" -> ctx.getTemperature() > rule.value();
                case "<" -> ctx.getTemperature() < rule.value();
                case "=" -> ctx.getTemperature() == rule.value();
                default -> false;
            };
        }

        // MOTION CHECK
        if (rule.sensor().equals("MOTION")) {
            System.out.println("Checking MOTION → motion=" + ctx.isMotionDetected());
            condition = ctx.isMotionDetected();
        }

        if (condition) {
            System.out.println("Condition MET → executing action");
            mediator.sendCommand(DeviceType.valueOf(rule.deviceType()), rule.action().equals("TURN_ON"));

            return rule.deviceType() + " -> " + rule.action().replace("TURN_", "");
        }

        System.out.println("Condition NOT MET → no action executed");
        return "Condition not met – no action executed";
    }
}
