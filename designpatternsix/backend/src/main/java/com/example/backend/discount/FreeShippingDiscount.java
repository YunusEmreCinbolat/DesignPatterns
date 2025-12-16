package com.example.backend.discount;

import com.example.backend.model.Cart;

public class FreeShippingDiscount implements Discount {

    private final double shippingCost;
    private final double minSubtotal;

    public FreeShippingDiscount(double shippingCost, double minSubtotal) {
        this.shippingCost = shippingCost;
        this.minSubtotal = minSubtotal;
    }

    @Override
    public double calculate(Cart cart) {
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Free shipping";
    }
}
