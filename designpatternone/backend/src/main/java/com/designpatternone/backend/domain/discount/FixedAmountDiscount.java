package com.designpatternone.backend.domain.discount;

import java.math.BigDecimal;

public class FixedAmountDiscount implements DiscountStrategy {

    @Override
    public String code() {
        return "FIX50";
    }

    @Override
    public BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping) {
        BigDecimal discounted = itemsTotal.subtract(BigDecimal.valueOf(50));
        return discounted.max(BigDecimal.ZERO).add(shipping); // negatif olmaması için
    }
}
