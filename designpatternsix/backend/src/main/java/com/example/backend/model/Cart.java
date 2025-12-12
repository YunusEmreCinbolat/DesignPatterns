package com.example.backend.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> items = new ArrayList<>();
    private double subtotal;
    private double discountAmount;
    private double finalTotal;

    public void addItem(CartItem item) {
        items.add(item);
        recalculateSubtotal();
    }

    private void recalculateSubtotal() {
        this.subtotal = items.stream()
                .mapToDouble(CartItem::getLineTotal)
                .sum();
        this.finalTotal = subtotal - discountAmount;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void applyDiscount(double discountAmount) {
        this.discountAmount = discountAmount;
        this.finalTotal = subtotal - discountAmount;
    }

    public int countProduct(String productId) {
        return items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public double getProductPrice(String productId) {
        return items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .map(i -> i.getProduct().getPrice())
                .findFirst()
                .orElse(0.0);
    }
}
