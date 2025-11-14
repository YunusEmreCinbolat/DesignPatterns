package com.example.backend.handler;

import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class TaxHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        double total = order.getTotalPrice();
        double tax = total * 0.20;
        total += tax;
        order.setTotalPrice(total);
        System.out.println("[TaxHandler] %20 KDV eklendi. Yeni toplam: " + total);
        return true;
    }
}
