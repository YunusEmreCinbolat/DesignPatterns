package com.example.backend.controller;

import com.example.backend.builder.PizzaBuilder;
import com.example.backend.dto.PizzaOrderRequest;
import com.example.backend.model.Order;
import com.example.backend.model.Pizza;
import com.example.backend.singleton.OrderManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderManager orderManager = OrderManager.getInstance();
    private final PizzaBuilder pizzaBuilder;

    public OrderController(PizzaBuilder pizzaBuilder) {
        this.pizzaBuilder = pizzaBuilder;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody PizzaOrderRequest request) {
        Pizza pizza = pizzaBuilder.buildPizza(request);
        Order order = orderManager.placeOrder(request.getCustomerName(), pizza);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderManager.getAllOrders();
        System.out.println("[API] Returning order list → count=" + orders.size());
        for (Order order : orders) {
            System.out.println("  - " + order);
        }
        return ResponseEntity.ok(orders);
    }
}
