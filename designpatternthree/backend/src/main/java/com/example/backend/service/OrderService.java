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

    // CHAIN HANDLER’LAR
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

        // ✔ CHAIN OF RESPONSIBILITY bağlama
        stockHandler
                .setNext(paymentHandler)
                .setNext(fraudHandler)
                .setNext(discountHandler)
                .setNext(taxHandler)
                .setNext(shippingHandler)
                .setNext(createOrderHandler);
    }

    /**
     * ✔ Composite Pattern → Ürünleri ProductBundle içinde topluyoruz
     * ✔ Proxy Pattern → PriceCacheProxy ile fiyat hesaplıyoruz
     * ✔ Chain of Responsibility → Siparişi adım adım işliyoruz
     * ✔ Repository → Siparişi kaydediyoruz
     */
    public void processOrder(Order order) {

        // 1) ✔ Composite → ürünleri bundle içinde toplama
        ProductBundle orderBundle = new ProductBundle("OrderBundle");

        for (OrderItem item : order.getItems()) {
            SingleProduct product = productRepository.findById(item.getProductId());
            if (product != null) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    orderBundle.add(product);
                }
            }
        }

        // 2) ✔ Proxy → fiyat hesaplama
        ProductComponent proxy = new PriceCacheProxy(orderBundle);
        double totalPrice = proxy.getPrice();
        order.setTotalPrice(totalPrice);

        System.out.println("[OrderService] Başlangıç toplam fiyat: " + totalPrice);

        // Varsayılan doğrulamalar
        order.setPaymentCompleted(true);
        order.setNotFraud(true);
        order.setAddressValid(true);

        // 3) ✔ Chain of Responsibility → siparişi adım adım işleme
        stockHandler.handle(order);

        System.out.println("[OrderService] İşlem sonrası durum: " + order.getStatus()
                + " | Toplam: " + order.getTotalPrice());

        // 4) ✔ Siparişi belleğe kaydetme (en önemli eksik buydu!)
        orderRepository.save(order);

        System.out.println("[OrderService] Sipariş kaydedildi → ID: " + order.getId());
    }
}
