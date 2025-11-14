package com.example.backend.handler;

import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class ShippingHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        double total = order.getTotalPrice();
        if (total >= 1000) {
            System.out.println("[ShippingHandler] Ücretsiz kargo uygulandı.");
        } else {
            total += 49.90;
            order.setTotalPrice(total);
            System.out.println("[ShippingHandler] 49.90₺ kargo eklendi. Yeni toplam: " + total);
        }
        return true;
    }
}
