package com.example.backend.order.status;

import com.example.backend.order.Order;
import com.example.backend.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class PreparingHandler extends BaseOrderStatusHandler {

    @Override
    public void handle(Order order) {
        if (order.getStatus() == OrderStatus.PREPARING) {
            order.setStatus(OrderStatus.SHIPPED);
            return;
        }
        forward(order);
    }
}
