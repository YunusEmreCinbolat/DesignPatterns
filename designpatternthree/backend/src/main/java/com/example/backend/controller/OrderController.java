package com.example.backend.controller;

import com.example.backend.order.Order;
import com.example.backend.order.OrderStatus;
import com.example.backend.repo.OrderRepository;
import com.example.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return order;
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) throw new RuntimeException("Order not found");
        return order;
    }

    @PostMapping("/{id}/next")
    public Order nextStatus(@PathVariable Long id) {

        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found: " + id);
        }

        // COMPLETED ise artık ilerlemesin
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return order; // aynen geri dön
        }

        switch (order.getStatus()) {
            case RECEIVED -> order.setStatus(OrderStatus.PREPARING);
            case PREPARING -> order.setStatus(OrderStatus.SHIPPED);
            case SHIPPED -> order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY -> order.setStatus(OrderStatus.DELIVERED);
            case DELIVERED -> order.setStatus(OrderStatus.COMPLETED);
            default -> { }
        }

        orderRepository.save(order);

        return order;
    }

}
