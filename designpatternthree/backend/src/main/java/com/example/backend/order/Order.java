package com.example.backend.order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Order {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.RECEIVED;
    private double totalPrice;

    // handler’ların ihtiyacı olan alanlar
    private boolean paymentCompleted;
    private boolean notFraud;
    private boolean addressValid;

    // reddetme için eklendi
    private String rejectionReason;

    public Order() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

    public Order(List<OrderItem> items) {
        this();
        this.items = items;
        calculateTotal();
    }

    public void calculateTotal() {
        this.totalPrice = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    // ✨ SİPARİŞ REDDEDİLDİĞİNDE ÇAĞRILIR
    public void markRejected(String reason) {
        this.status = OrderStatus.REJECTED;
        this.rejectionReason = reason;
        this.paymentCompleted = false;
    }

    // GETTER / SETTER’lar
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotal();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
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

    public String getRejectionReason() {
        return rejectionReason;
    }
}
