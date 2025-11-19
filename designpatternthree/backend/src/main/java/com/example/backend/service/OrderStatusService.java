package com.example.backend.service;

import com.example.backend.order.Order;
import com.example.backend.order.status.*;
import org.springframework.stereotype.Service;
@Service
public class OrderStatusService {

    private final ReceivedHandler h1;
    private final PreparingHandler h2;
    private final ShippedHandler h3;
    private final OutForDeliveryHandler h4;
    private final DeliveredHandler h5;

    public OrderStatusService(
            ReceivedHandler h1,
            PreparingHandler h2,
            ShippedHandler h3,
            OutForDeliveryHandler h4,
            DeliveredHandler h5
    ) {
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;

        h1.setNext(h2);
        h2.setNext(h3);
        h3.setNext(h4);
        h4.setNext(h5);  // ✔ SON HALKA
    }

    public Order nextStep(Order order) {
        h1.handle(order);
        return order;
    }
}
