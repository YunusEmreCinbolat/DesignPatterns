package com.example.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemRequest {

    private String productId;
    private String name;
    private double price;
    private int quantity;
}
