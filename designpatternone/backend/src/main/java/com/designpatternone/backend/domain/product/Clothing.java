package com.designpatternone.backend.domain.product;


// domain/product/Clothing.java
import java.math.BigDecimal;
public class Clothing extends Product {

    private final String size;

    public Clothing(String id, String name, BigDecimal price, String size){
        super(id, name, price); this.size = size;
    }

    public String getSize(){ return size; }

    @Override public String getType(){ return "CLOTHING"; }
}
