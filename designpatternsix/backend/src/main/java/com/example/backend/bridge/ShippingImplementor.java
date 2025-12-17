package com.example.backend.bridge;

import com.example.backend.model.Cart;

public interface ShippingImplementor {
    double calculateShipping(Cart cart);
    String getName();
}
