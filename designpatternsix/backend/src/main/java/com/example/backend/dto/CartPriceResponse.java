package com.example.backend.dto;

public class CartPriceResponse {

    private double subtotal;
    private double discount;
    private double finalTotal;
    private String description;

    public CartPriceResponse(double subtotal, double discount, double finalTotal, String description) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.finalTotal = finalTotal;
        this.description = description;
    }

    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getFinalTotal() { return finalTotal; }
    public String getDescription() { return description; }
}
