package com.example.backend.controller;

import com.example.backend.order.Order;
import com.example.backend.repo.OrderRepository;
import com.example.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Frontend için CORS açıyoruz
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

   
    @PostMapping
    public Order createOrder(@RequestBody Order order) {

        // Siparişi işleme al
        orderService.processOrder(order);

        // Frontend'e Order objesini direkt dönderiyoruz
        return order;
    }


    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found: " + id);
        }
        return order;
    }
}
