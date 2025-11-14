package com.example.backend.repo;


import com.example.backend.order.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {

    private final Map<Long, Order> store = new HashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public Order save(Order order) {
        if (order.getId() == null) {
            Long id = idSeq.getAndIncrement();
            order.setId(id);
        }
        store.put(order.getId(), order);
        return order;
    }

    public Order findById(Long id) {
        return store.get(id);
    }
}
