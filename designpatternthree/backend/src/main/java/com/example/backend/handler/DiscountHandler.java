package com.example.backend.handler;


import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class DiscountHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        double total = order.getTotalPrice();
        if (total >= 20000) {
            double discount = total * 0.05;
            total -= discount;
            order.setTotalPrice(total);
            System.out.println("[DiscountHandler] %5 indirim uygulandı. Yeni toplam: " + total);
        } else {
            System.out.println("[DiscountHandler] İndirim uygulanmadı (tutar yetersiz).");
        }
        return true;
    }
}
