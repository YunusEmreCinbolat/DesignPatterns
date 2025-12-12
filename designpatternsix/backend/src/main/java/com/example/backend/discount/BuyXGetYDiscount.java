package com.example.backend.discount;

import com.example.backend.model.Cart;

public class BuyXGetYDiscount implements Discount {

    private final String productId;
    private final int x;
    private final int y;

    public BuyXGetYDiscount(String productId, int x, int y) {
        this.productId = productId;
        this.x = x;
        this.y = y;
    }

    @Override
    public double calculate(Cart cart) {
        int count = cart.countProduct(productId);
        if (count >= x) {
            int freeItems = count / x * y;
            double unitPrice = cart.getProductPrice(productId);
            double discount = unitPrice * freeItems;
            System.out.println("[DISCOUNT] Buy " + x + " Get " + y + " applied → " + discount);
            return discount;
        }
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Buy " + x + " Get " + y + " free for product " + productId;
    }
}
