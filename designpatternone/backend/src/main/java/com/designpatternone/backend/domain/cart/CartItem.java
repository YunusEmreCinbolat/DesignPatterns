package com.designpatternone.backend.domain.cart;


// domain/cart/CartItem.java

import com.designpatternone.backend.domain.product.Product;

import java.math.BigDecimal;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity){
        this.product = product; this.quantity = quantity;
    }

    public Product getProduct(){ return product; }

    public int getQuantity(){ return quantity; }

    public void setQuantity(int q){ this.quantity = Math.max(1, q); }

    public BigDecimal getSubtotal(){
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
