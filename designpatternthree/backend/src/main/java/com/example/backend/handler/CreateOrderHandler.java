package com.example.backend.handler;

import com.example.backend.order.Order;
import com.example.backend.order.OrderStatus;
import com.example.backend.repo.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderHandler extends OrderHandler {

    private final OrderRepository orderRepository;

    public CreateOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected boolean doHandle(Order order) {
        if (order.getStatus() == OrderStatus.REJECTED) {
            System.out.println("[CreateOrderHandler] Sipariş reddedildiği için kaydedilmeyecek.");
            return false;
        }

        orderRepository.save(order);
        System.out.println("[CreateOrderHandler] Sipariş başarıyla oluşturuldu. Durum: COMPLETED");
        return true;
    }
}
