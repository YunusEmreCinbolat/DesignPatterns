package com.example.backend.facade;

import com.example.backend.discount.*;
import com.example.backend.enums.DiscountType;
import com.example.backend.model.Cart;
import com.example.backend.model.DiscountResult;

public class DiscountFacade {

    public DiscountResult applyDiscount(Cart cart, DiscountType type) {

        Discount discountStrategy = resolveStrategy(type);

        double discountAmount = discountStrategy.calculate(cart);
        cart.applyDiscount(discountAmount);

        return new DiscountResult(
                cart.getSubtotal(),
                discountAmount,
                cart.getFinalTotal(),
                discountStrategy.getDescription()
        );
    }

    private Discount resolveStrategy(DiscountType type) {

        return switch (type) {
            case NONE -> new Discount() {
                @Override
                public double calculate(Cart cart) { return 0.0; }
                @Override
                public String getDescription() { return "No discount"; }
            };
            case PERCENTAGE -> new PercentageDiscount(20); // örnek: sabit %20
            case BUY_X_GET_Y -> new BuyXGetYDiscount("P1", 3, 1); // ürün P1 için 3 al 1 bedava
            case FREE_SHIPPING -> new FreeShippingDiscount(30, 200); // 200 üzeri 30 TL kargo bedava
        };
    }
}
