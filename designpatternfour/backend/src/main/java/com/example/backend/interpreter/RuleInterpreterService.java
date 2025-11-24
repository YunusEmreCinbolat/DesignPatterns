package com.example.backend.interpreter;


import com.example.backend.device.DeviceType;
import com.example.backend.interpreter.dto.RuleRequest;
import com.example.backend.mediator.SmartHomeMediator;

public class RuleInterpreterService {

    private final SmartHomeMediator mediator;

    public RuleInterpreterService(SmartHomeMediator mediator) {
        this.mediator = mediator;
    }

    public void evaluateAndExecute(RuleRequest rule, Context ctx) {

        boolean conditionMet = false;

        if ("TEMPERATURE".equals(rule.sensor()) && ">".equals(rule.operator())) {
            conditionMet = ctx.getTemperature() > rule.value();
        }

        if ("MOTION".equals(rule.sensor())) {
            conditionMet = ctx.isMotionDetected();
        }

        if (conditionMet) {
            DeviceType type = DeviceType.valueOf(rule.deviceType());
            boolean turnOn = "TURN_ON".equals(rule.action());
            mediator.sendCommand(type, turnOn);
        }
    }
}
