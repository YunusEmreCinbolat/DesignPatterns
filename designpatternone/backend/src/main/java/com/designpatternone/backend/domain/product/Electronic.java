package com.designpatternone.backend.domain.product;

// domain/product/Electronic.java
import java.math.BigDecimal;
public class Electronic extends Product {

    private final int warrantyMonths;

    public Electronic(String id, String name, BigDecimal price, int warrantyMonths){
        super(id, name, price); this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths(){ return warrantyMonths; }

    @Override public String getType(){ return "ELECTRONIC"; }
}
