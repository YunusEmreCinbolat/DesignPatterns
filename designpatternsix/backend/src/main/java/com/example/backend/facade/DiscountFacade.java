package com.example.backend.facade;

import com.example.backend.bridge.*;
import com.example.backend.discount.*;
import com.example.backend.enums.DiscountType;
import com.example.backend.model.Cart;
import com.example.backend.model.DiscountResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiscountFacade {

    private static final double SHIPPING_FEE = 50.0;

    public DiscountResult applyDiscount(Cart cart, DiscountType type) {

        log.info("[FACADE] DiscountFacade orchestrating pricing (Strategy + Bridge)");
        log.debug("[FACADE] Input → items={}, subtotal={}, discountType={}", cart.getItems().size(), cart.getSubtotal(), type);

        Discount discountStrategy = resolveStrategy(type);

        log.debug("[STRATEGY] Selected discount strategy → {}", discountStrategy.getClass().getSimpleName());

        double discountAmount = discountStrategy.calculate(cart);
        cart.applyDiscount(discountAmount);

        double totalAfterDiscount = cart.getFinalTotal();

        ShippingImplementor shipping = (type == DiscountType.FREE_SHIPPING)
            ? new FreeShipping()
            : new FlatRateShipping(SHIPPING_FEE);
        Checkout checkout = new StandardCheckout(shipping);

        log.debug("[BRIDGE] Checkout abstraction → {} | Shipping implementor → {}",
            checkout.getClass().getSimpleName(),
            shipping.getClass().getSimpleName());

        double shippingFee = checkout.calculateShippingFee(cart);
        double finalTotalWithShipping = checkout.calculateFinalTotal(totalAfterDiscount, cart);

        log.info("[FACADE] Breakdown → subtotal={}, discount={}, afterDiscount={}, shipping={}, finalTotal={}",
            cart.getSubtotal(),
            discountAmount,
            totalAfterDiscount,
            shippingFee,
            finalTotalWithShipping);

        return new DiscountResult(
                cart.getSubtotal(),
                discountAmount,
                totalAfterDiscount,
                shippingFee,
                finalTotalWithShipping,
                discountStrategy.getDescription()
        );
    }

    private Discount resolveStrategy(DiscountType type) {

        log.debug("[STRATEGY] Resolving discount strategy for type={}", type);
        return switch (type) {
            case NONE -> new Discount() {
                @Override
                public double calculate(Cart cart) { return 0.0; }
                @Override
                public String getDescription() { return "No discount"; }
            };
            case PERCENTAGE -> new PercentageDiscount(20); // örnek: sabit %20
            case BUY_X_GET_Y -> new BuyXGetYDiscount("P1", 3, 1); // ürün P1 için 3 al 1 bedava
            case FREE_SHIPPING -> new FreeShippingDiscount(0, 0);
        };
    }
}
