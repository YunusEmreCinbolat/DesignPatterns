package com.example.backend.dto;

import com.example.backend.enums.DiscountType;

import java.util.List;

public class CartPriceRequest {

    private List<CartItemRequest> items;
    private DiscountType discountType;

    public List<CartItemRequest> getItems() { return items; }
    public void setItems(List<CartItemRequest> items) { this.items = items; }

    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
}
