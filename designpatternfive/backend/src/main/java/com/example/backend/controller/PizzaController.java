package com.example.backend.controller;

import com.example.backend.dto.PizzaOrderRequest;
import com.example.backend.enums.DoughType;
import com.example.backend.enums.SauceType;
import com.example.backend.enums.Size;
import com.example.backend.model.Order;
import com.example.backend.model.Pizza;
import com.example.backend.singleton.OrderManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@CrossOrigin(origins = "http://localhost:4200")
public class PizzaController {

    private final OrderManager orderManager = OrderManager.getInstance(); // Singleton kullanımı

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody PizzaOrderRequest request) {

        System.out.println("[API] Incoming order request → " + request.getCustomerName());

        Pizza pizza = new Pizza.Builder()
                .size(Size.valueOf(request.getSize()))
                .doughType(DoughType.valueOf(request.getDoughType()))
                .sauceType(SauceType.valueOf(request.getSauceType()))
                .spicy(request.isSpicy())
                .build();

        if (request.getToppings() != null) {
            for (String topping : request.getToppings()) {
                pizza = new Pizza.Builder()
                        .size(pizza.getSize())
                        .doughType(pizza.getDoughType())
                        .sauceType(pizza.getSauceType())
                        .spicy(pizza.isSpicy())
                        .addTopping(topping)
                        .build();
            }
        }

        Order order = orderManager.placeOrder(request.getCustomerName(), pizza);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> orders = orderManager.getAllOrders();
        System.out.println("[API] Returning " + orders.size() + " orders");
        return ResponseEntity.ok(orders);
    }
}
