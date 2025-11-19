package com.example.backend.handler;

import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class PaymentHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        if (!order.isPaymentCompleted()) {
            System.out.println("[PaymentHandler] Ödeme başarısız. Sipariş reddedildi.");
            order.markRejected("");
            return false;
        }
        System.out.println("[PaymentHandler] Ödeme kontrolü geçti.");
        return true;
    }
}
