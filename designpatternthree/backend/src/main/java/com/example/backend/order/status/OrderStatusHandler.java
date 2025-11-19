package com.example.backend.order.status;


import com.example.backend.order.Order;

public interface OrderStatusHandler {
    void handle(Order order);
    void setNext(OrderStatusHandler next);
}
