package com.example.backend.order.status;

import com.example.backend.order.Order;

public abstract class BaseOrderStatusHandler implements OrderStatusHandler {

    protected OrderStatusHandler next;

    @Override
    public void setNext(OrderStatusHandler next) {
        this.next = next;
    }

    protected void forward(Order order) {
        if (next != null) {
            next.handle(order);
        }
    }
}
