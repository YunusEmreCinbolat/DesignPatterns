package com.example.backend.handler;


import com.example.backend.order.Order;

public abstract class OrderHandler {

    protected OrderHandler next;

    public OrderHandler setNext(OrderHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(Order order) {
        if (doHandle(order) && next != null) {
            next.handle(order);
        }
    }

    protected abstract boolean doHandle(Order order);
}
