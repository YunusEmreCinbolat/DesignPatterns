package com.example.backend.dto;

import com.example.backend.enums.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartPriceRequest {

    private List<CartItemRequest> items;
    private DiscountType discountType;
}
