package com.example.backend.controller;

import com.example.backend.order.Order;
import com.example.backend.repo.OrderRepository;
import com.example.backend.service.OrderService;
import com.example.backend.service.OrderStatusService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;

    public OrderController(
            OrderRepository orderRepository,
            OrderService orderService,
            OrderStatusService orderStatusService
    ) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {

        System.out.println("[OrderController] Sipariş alındı → " + order.getId());

        // ✔ CHAIN OF RESPONSIBILITY (Sipariş oluşturma chain’i)
        orderService.processOrder(order);

        orderRepository.save(order);

        System.out.println("[OrderController] Sipariş kaydedildi → " + order.getId());

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

        System.out.println("\n----------------------------------------");
        System.out.println("[OrderStatus] Şu anki durum → " + order.getStatus());

        // ✔ CHAIN OF RESPONSIBILITY (Sipariş DURUM chain’i)
        orderStatusService.nextStep(order);

        System.out.println("[OrderStatus] Yeni durum → " + order.getStatus());
        System.out.println("----------------------------------------\n");

        orderRepository.save(order);

        return order;
    }
}
