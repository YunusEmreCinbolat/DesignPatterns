package com.designpatternone.backend.domain.discount;

import java.math.BigDecimal;

public class FreeShippingDiscount implements DiscountStrategy {

    @Override
    public String code() {
        return "FREESHIP";
    }

    @Override
    public BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping) {
        return itemsTotal; // kargo ücreti düşülmüyor
    }

    @Override
    public BigDecimal effectiveShipping(BigDecimal shipping) {
        return BigDecimal.ZERO; // kargo sıfır
    }
}
