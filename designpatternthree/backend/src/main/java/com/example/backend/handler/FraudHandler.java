package com.example.backend.handler;

import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class FraudHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        if (!order.isNotFraud()) {
            System.out.println("[FraudHandler] Fraud şüphesi. Sipariş reddedildi.");
            order.markRejected("");
            return false;
        }
        System.out.println("[FraudHandler] Fraud kontrolü geçti.");
        return true;
    }
}
