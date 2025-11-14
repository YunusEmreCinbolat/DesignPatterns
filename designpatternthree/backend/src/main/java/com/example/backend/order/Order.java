package com.example.backend.order;


import java.util.ArrayList;
import java.util.List;

/**
 * Sipariş modeli.
 * Basit tuttuk: normalde burada kullanıcı, adres, ödeme bilgisi vs. de olur.
 */
public class Order {

    private Long id;
    private List<OrderItem> items = new ArrayList<>();
    private double totalPrice;
    private OrderStatus status = OrderStatus.PENDING;

    // Örnek flag alanlar (handler'lar bunlara bakacak)
    private boolean paymentCompleted;
    private boolean notFraud;
    private boolean addressValid;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markRejected() {
        this.status = OrderStatus.REJECTED;
    }

    public void markCompleted() {
        this.status = OrderStatus.COMPLETED;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public boolean isNotFraud() {
        return notFraud;
    }

    public void setNotFraud(boolean notFraud) {
        this.notFraud = notFraud;
    }

    public boolean isAddressValid() {
        return addressValid;
    }

    public void setAddressValid(boolean addressValid) {
        this.addressValid = addressValid;
    }
}
