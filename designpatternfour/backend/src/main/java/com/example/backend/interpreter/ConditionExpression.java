package com.example.backend.interpreter;


/**
 * Terminal Expression
 *
 * Sensör + Operatör + Değer kontrolünü yapar.
 */
public class ConditionExpression {

    private final String sensor;
    private final String operator;
    private final int value;

    public ConditionExpression(String sensor, String operator, int value) {
        this.sensor = sensor;
        this.operator = operator;
        this.value = value;
    }

    public boolean interpret(Context ctx) {

        if(sensor.equalsIgnoreCase("TEMPERATURE")) {
            return operator.equals(">")
                    ? ctx.getTemperature() > value
                    : ctx.getTemperature() < value;
        }

        if(sensor.equalsIgnoreCase("MOTION")) {
            return ctx.isMotionDetected();
        }

        return false;
    }
}
