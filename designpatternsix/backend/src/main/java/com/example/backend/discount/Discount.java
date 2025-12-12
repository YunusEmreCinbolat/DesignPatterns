package com.example.backend.discount;

import com.example.backend.model.Cart;

public interface Discount {
    double calculate(Cart cart);
    String getDescription();
}
