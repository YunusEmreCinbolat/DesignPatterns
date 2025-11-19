package com.example.backend.order.status;

import com.example.backend.order.Order;
import com.example.backend.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class DeliveredHandler extends BaseOrderStatusHandler {

    @Override
    public void handle(Order order) {
        // Son durum: Yapılacak işlem yok
        if (order.getStatus() == OrderStatus.DELIVERED) {
            return;
        }

        // Eğer başka bir handler işleyemediyse zinciri sonlandır
        forward(order);
    }
}
