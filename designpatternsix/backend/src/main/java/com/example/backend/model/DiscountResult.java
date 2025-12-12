package com.example.backend.model;

public class DiscountResult {

    private final double subtotal;
    private final double discountAmount;
    private final double finalTotal;
    private final String description;

    public DiscountResult(double subtotal, double discountAmount, double finalTotal, String description) {
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.finalTotal = finalTotal;
        this.description = description;
    }

    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalTotal() { return finalTotal; }
    public String getDescription() { return description; }
}
