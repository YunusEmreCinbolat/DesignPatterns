package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartPriceResponse {

    private double subtotal;
    private double discount;
    private double totalAfterDiscount;
    private double shippingFee;
    private double finalTotal;
    private String description;
}
