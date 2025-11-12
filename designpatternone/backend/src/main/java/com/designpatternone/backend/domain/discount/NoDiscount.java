package com.designpatternone.backend.domain.discount;

import java.math.BigDecimal;

public class NoDiscount implements DiscountStrategy {

    @Override
    public String code() {
        return "NONE";
    }

    @Override
    public BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping) {
        System.out.println("İndirim uygulanmadı.");
        return itemsTotal.add(shipping);
    }
}
