package com.example.backend.service;

import com.example.backend.handler.*;
import com.example.backend.order.Order;
import com.example.backend.order.OrderItem;
import com.example.backend.pricing.PriceCacheProxy;
import com.example.backend.product.ProductBundle;
import com.example.backend.product.ProductComponent;
import com.example.backend.product.SingleProduct;
import com.example.backend.repo.OrderRepository;
import com.example.backend.repo.ProductRepository;
import org.springframework.stereotype.Service;
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final StockHandler stockHandler;
    private final PaymentHandler paymentHandler;
    private final FraudHandler fraudHandler;
    private final DiscountHandler discountHandler;
    private final TaxHandler taxHandler;
    private final ShippingHandler shippingHandler;
    private final CreateOrderHandler createOrderHandler;

    public OrderService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            StockHandler stockHandler,
            PaymentHandler paymentHandler,
            FraudHandler fraudHandler,
            DiscountHandler discountHandler,
            TaxHandler taxHandler,
            ShippingHandler shippingHandler,
            CreateOrderHandler createOrderHandler
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;

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

        System.out.println("----------------------------------------------------");
        System.out.println("[OrderService] 1) Composite Pattern → Ürünleri birleştiriliyor...");

        ProductBundle bundle = new ProductBundle("OrderBundle");

        for (OrderItem item : order.getItems()) {
            SingleProduct product = productRepository.findById(item.getProductId());

            if (product != null) {
                System.out.println("   → " + item.getProductId() + " x " + item.getQuantity() + " eklendi.");
                for (int i = 0; i < item.getQuantity(); i++) {
                    bundle.add(product);
                }
            }
        }

        System.out.println("[OrderService] Composite tamamlandı.");

        System.out.println("\n[OrderService] 2) Proxy Pattern → Fiyat hesaplanıyor (PriceCacheProxy)...");
        ProductComponent proxy = new PriceCacheProxy(bundle);
        double totalPrice = proxy.getPrice();

        System.out.println("   → İlk toplam fiyat: " + totalPrice);
        order.setTotalPrice(totalPrice);

        order.setPaymentCompleted(true);
        order.setNotFraud(true);
        order.setAddressValid(true);

        System.out.println("\n[OrderService] 3) Chain of Responsibility başlıyor...");
        stockHandler.handle(order);

        System.out.println("[OrderService] Chain tamamlandı. Son Durum: " + order.getStatus());
        System.out.println("[OrderService] Nihai Fiyat: " + order.getTotalPrice());

        orderRepository.save(order);
        System.out.println("[OrderService] ✔ Sipariş kaydedildi → ID: " + order.getId());
        System.out.println("----------------------------------------------------\n");
    }
}
