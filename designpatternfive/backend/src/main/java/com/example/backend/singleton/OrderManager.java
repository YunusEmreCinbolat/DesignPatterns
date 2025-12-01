package com.example.backend.singleton;

import com.example.backend.model.Order;
import com.example.backend.model.Pizza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderManager {

    private static OrderManager instance;

    private final List<Order> orders = new ArrayList<>();
    private long orderSequence = 1L;

    private OrderManager() {
        // private constructor → dışarıdan newlenemesin
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
            System.out.println("[SINGLETON] OrderManager instance created");
        }
        return instance;
    }

    public synchronized Order placeOrder(String customerName, Pizza pizza) {
        Order order = new Order(orderSequence++, customerName, pizza);
        orders.add(order);
        System.out.println("[SINGLETON] New order placed → " + order);
        return order;
    }

    public List<Order> getAllOrders() {
        return Collections.unmodifiableList(orders);
    }
}
