package com.example.backend.interpreter;

import com.example.backend.device.DeviceType;
import com.example.backend.interpreter.dto.RuleRequest;
import com.example.backend.mediator.SmartHomeMediator;

/**
 * Interpreter Pattern – Expression class
 *
 * Bu sınıf bir kuralın (condition + action + device)
 * nasıl yorumlanacağını tanımlar.
 */
public class RuleExpression {

    private final ConditionExpression conditionExpression;
    private final String action;
    private final DeviceType deviceType;

    /**
     * Kullanıcıdan gelen JSON RuleRequest üzerinden Expression üretir
     */
    public RuleExpression(RuleRequest request) {
        this.conditionExpression = new ConditionExpression(
                request.sensor(),
                request.operator(),
                request.value()
        );
        this.action = request.action();
        this.deviceType = DeviceType.valueOf(request.deviceType());
    }

    /**
     * Interpreter’ın çalıştırıldığı yer
     */
    public void interpret(Context ctx, SmartHomeMediator mediator) {

        boolean result = conditionExpression.interpret(ctx);

        if(result) {
            boolean turnOn = action.equalsIgnoreCase("TURN_ON");
            mediator.sendCommand(deviceType, turnOn);
        }
    }
}
