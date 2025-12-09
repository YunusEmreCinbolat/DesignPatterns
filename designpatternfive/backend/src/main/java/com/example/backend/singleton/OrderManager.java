package com.example.backend.singleton;

import com.example.backend.model.Order;
import com.example.backend.model.Pizza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderManager {

    private static OrderManager instance;   // Singleton instance

    private final List<Order> orders = new ArrayList<>();
    private long orderSequence = 1L;

    private OrderManager() {
        System.out.println("[SINGLETON] OrderManager instance initialized");
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
            System.out.println("[SINGLETON] OrderManager created");
        } else {
            System.out.println("[SINGLETON] Existing OrderManager reused");
        }
        return instance;
    }

    public synchronized Order placeOrder(String customerName, Pizza pizza) {
        System.out.println("[SINGLETON] Saving new order...");
        System.out.println(" → Customer: " + customerName);
        System.out.println(" → Pizza: " + pizza);

        Order order = new Order((int) orderSequence++, customerName, pizza);
        orders.add(order);

        System.out.println("[SINGLETON] Order stored: " + order);
        return order;
    }

    public List<Order> getAllOrders() {
        System.out.println("[SINGLETON] Returning all orders (count = " + orders.size() + ")");
        return Collections.unmodifiableList(orders);
    }
}
