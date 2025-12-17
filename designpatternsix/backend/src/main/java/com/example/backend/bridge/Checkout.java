package com.example.backend.bridge;

import com.example.backend.model.Cart;

public abstract class Checkout {

    protected final ShippingImplementor shipping;

    protected Checkout(ShippingImplementor shipping) {
        this.shipping = shipping;
    }

    public double calculateShippingFee(Cart cart) {
        return shipping.calculateShipping(cart);
    }

    public double calculateFinalTotal(double totalAfterDiscount, Cart cart) {
        return totalAfterDiscount + calculateShippingFee(cart);
    }

    public String getShippingName() {
        return shipping.getName();
    }
}
