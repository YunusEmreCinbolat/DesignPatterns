package com.example.backend.bridge;

import com.example.backend.model.Cart;

public class FreeShipping implements ShippingImplementor {

    @Override
    public double calculateShipping(Cart cart) {
        return 0.0;
    }

    @Override
    public String getName() {
        return "Free shipping";
    }
}
