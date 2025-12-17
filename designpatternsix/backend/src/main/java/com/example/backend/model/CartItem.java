package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItem {

    private final Product product;
    private final int quantity;

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
