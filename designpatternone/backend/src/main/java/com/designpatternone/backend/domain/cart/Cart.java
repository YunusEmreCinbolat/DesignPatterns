package com.designpatternone.backend.domain.cart;


// domain/cart/Cart.java

import java.math.BigDecimal;
import java.util.*;

public class Cart {
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    private BigDecimal shipping = new BigDecimal("29.90"); // örnek sabit kargo

    public Collection<CartItem> getItems(){
        return items.values();
    }

    public BigDecimal getItemsTotal(){
        return items.values().stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getShipping(){ return shipping; }
    public void setShipping(BigDecimal s){ this.shipping = s.max(BigDecimal.ZERO); }

    public void add(String productId, CartItem item){
        items.merge(productId, item, (oldI, newI) -> { oldI.setQuantity(oldI.getQuantity()+newI.getQuantity()); return oldI; });
    }
    public void remove(String productId){
        items.remove(productId);
    }

    public void clear(){
        items.clear(); shipping = new BigDecimal("29.90");
    }
}
