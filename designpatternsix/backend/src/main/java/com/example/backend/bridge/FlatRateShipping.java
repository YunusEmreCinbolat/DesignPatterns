package com.example.backend.bridge;

import com.example.backend.model.Cart;

public class FlatRateShipping implements ShippingImplementor {

    private final double fee;

    public FlatRateShipping(double fee) {
        this.fee = fee;
    }

    @Override
    public double calculateShipping(Cart cart) {
        return fee;
    }

    @Override
    public String getName() {
        return "Flat rate";
    }
}
