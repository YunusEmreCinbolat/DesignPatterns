package com.designpatternone.backend.factory;

import com.designpatternone.backend.domain.discount.*;

public class DiscountFactory {

    public static DiscountStrategy getDiscountStrategy(DiscountType type) {
        if (type == null) {
            return new NoDiscount();
        }

        switch (type) {
            case PERCENTAGE:
                return new PercentageDiscount();
            case FIXED_AMOUNT:
                return new FixedAmountDiscount();
            case FREE_SHIPPING:
                return new FreeShippingDiscount();
            default:
                return new NoDiscount();
        }
    }

    public static DiscountStrategy fromCode(String code) {
        if (code == null) return new NoDiscount();

        switch (code.toUpperCase()) {
            case "PERC10": return new PercentageDiscount();
            case "FIX50": return new FixedAmountDiscount();
            case "FREESHIP": return new FreeShippingDiscount();
            default: return new NoDiscount();
        }
    }
}
