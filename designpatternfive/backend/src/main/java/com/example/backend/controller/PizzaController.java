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

        Pizza.Builder builder = new Pizza.Builder()
                .size(Size.valueOf(request.getSize()))
                .doughType(DoughType.valueOf(request.getDoughType()))
                .sauceType(SauceType.valueOf(request.getSauceType()))
                .spicy(request.isSpicy());

        if (request.getToppings() != null) {
            for (String topping : request.getToppings()) {
                System.out.println("[API] Adding topping → " + topping);
                builder.addTopping(topping);
            }
        }

        Pizza pizza = builder.build();

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
