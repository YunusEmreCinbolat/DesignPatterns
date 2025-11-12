package com.designpatternone.backend.domain.discount;


// domain/discount/DiscountStrategy.java

import java.math.BigDecimal;

public interface DiscountStrategy {
    String code(); // "PERC10", "FIX50", "FREESHIP" gibi

    BigDecimal applyTo(BigDecimal itemsTotal, BigDecimal shipping);

    default BigDecimal effectiveShipping(BigDecimal shipping){ return shipping; } // bazı stratejiler kargoyu değiştirir
}
