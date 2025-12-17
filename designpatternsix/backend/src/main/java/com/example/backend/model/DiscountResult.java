package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscountResult {

    private final double subtotal;
    private final double discountAmount;
    private final double totalAfterDiscount;
    private final double shippingFee;
    private final double finalTotal;
    private final String description;
}
