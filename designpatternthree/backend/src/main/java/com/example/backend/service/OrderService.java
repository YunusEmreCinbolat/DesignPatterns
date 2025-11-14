package com.example.backend.service;

// src/main/java/com/example/backend/service/OrderService.java

import com.example.backend.handler.*;
import com.example.backend.order.Order;
import com.example.backend.order.OrderItem;
import com.example.backend.pricing.PriceCacheProxy;
import com.example.backend.product.ProductBundle;
import com.example.backend.product.ProductComponent;
import com.example.backend.product.SingleProduct;
import com.example.backend.repo.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    private final StockHandler stockHandler;
    private final PaymentHandler paymentHandler;
    private final FraudHandler fraudHandler;
    private final DiscountHandler discountHandler;
    private final TaxHandler taxHandler;
    private final ShippingHandler shippingHandler;
    private final CreateOrderHandler createOrderHandler;

    public OrderService(
            ProductRepository productRepository,
            StockHandler stockHandler,
            PaymentHandler paymentHandler,
            FraudHandler fraudHandler,
            DiscountHandler discountHandler,
            TaxHandler taxHandler,
            ShippingHandler shippingHandler,
            CreateOrderHandler createOrderHandler
    ) {
        this.productRepository = productRepository;
        this.stockHandler = stockHandler;
        this.paymentHandler = paymentHandler;
        this.fraudHandler = fraudHandler;
        this.discountHandler = discountHandler;
        this.taxHandler = taxHandler;
        this.shippingHandler = shippingHandler;
        this.createOrderHandler = createOrderHandler;

        stockHandler
                .setNext(paymentHandler)
                .setNext(fraudHandler)
                .setNext(discountHandler)
                .setNext(taxHandler)
                .setNext(shippingHandler)
                .setNext(createOrderHandler);
    }

    public void processOrder(Order order) {
        ProductBundle orderBundle = new ProductBundle("OrderBundle");

        for (OrderItem item : order.getItems()) {
            SingleProduct product = productRepository.findById(item.getProductId());
            if (product != null) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    orderBundle.add(product);
                }
            }
        }

        ProductComponent proxy = new PriceCacheProxy(orderBundle);
        double totalPrice = proxy.getPrice();
        order.setTotalPrice(totalPrice);

        System.out.println("[OrderService] Başlangıç toplam fiyat: " + totalPrice);

        order.setPaymentCompleted(true);
        order.setNotFraud(true);
        order.setAddressValid(true);

        stockHandler.handle(order);

        System.out.println("[OrderService] Nihai durum: " + order.getStatus()
                + ", Toplam: " + order.getTotalPrice());
    }
}
