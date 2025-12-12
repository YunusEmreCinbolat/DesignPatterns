package com.example.backend.discount;

import com.example.backend.model.Cart;

public class PercentageDiscount implements Discount {

    private final double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double calculate(Cart cart) {
        double discount = cart.getSubtotal() * (percent / 100.0);
        System.out.println("[DISCOUNT] Percentage " + percent + "% → " + discount);
        return discount;
    }

    @Override
    public String getDescription() {
        return "Percentage discount (" + percent + "%)";
    }
}
