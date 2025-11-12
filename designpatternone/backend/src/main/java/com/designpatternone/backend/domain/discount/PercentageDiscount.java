package com.designpatternone.backend.domain.discount;

import java.math.BigDecimal;

public class PercentageDiscount implements DiscountStrategy {

    @Override
    public String code() {
        return "PERC10";
    }

    @Override
    public BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping) {
        BigDecimal discount = itemsTotal.multiply(BigDecimal.valueOf(0.10)); // %10 indirim
        return itemsTotal.subtract(discount).add(shipping);
    }
}
