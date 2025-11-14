package com.example.backend.handler;

import com.example.backend.order.Order;
import org.springframework.stereotype.Component;

@Component
public class StockHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        System.out.println("[StockHandler] Stok kontrolü yapıldı. (varsayılan: OK)");
        return true;
    }
}
