package com.designpatternone.backend.domain.product;

import java.math.BigDecimal;

public abstract class Product {
    protected String id;
    protected String name;
    protected BigDecimal price;

    protected Product(String id, String name, BigDecimal price) {
        this.id = id; this.name = name; this.price = price;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public abstract String getType();
}
