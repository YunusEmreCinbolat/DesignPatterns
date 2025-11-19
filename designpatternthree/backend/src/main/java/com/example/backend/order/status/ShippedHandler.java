package com.example.backend.order.status;

import com.example.backend.order.Order;
import com.example.backend.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class ShippedHandler extends BaseOrderStatusHandler {

    @Override
    public void handle(Order order) {
        if (order.getStatus() == OrderStatus.SHIPPED) {
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            return;
        }
        forward(order);
    }
}
