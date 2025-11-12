package com.designpatternone.backend.service;

import com.designpatternone.backend.domain.cart.Cart;
import com.designpatternone.backend.domain.cart.CartItem;
import com.designpatternone.backend.domain.discount.DiscountStrategy;
import com.designpatternone.backend.domain.product.Product;
import com.designpatternone.backend.factory.DiscountFactory;
import com.designpatternone.backend.repo.InMemoryProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final Cart cart = new Cart();
    private final InMemoryProductRepo repo = new InMemoryProductRepo();
    private DiscountStrategy strategy = null;

    public Cart get(){ return cart; }

    public void add(String productId, int qty){
        Product p = repo.findById(productId).orElseThrow();
        cart.add(productId, new CartItem(p, qty));
    }

    public void remove(String productId){ cart.remove(productId); }

    public void clear(){
        cart.clear();
        strategy = null;
    }

    public Map<String, Object> totals() {
        BigDecimal itemsTotal = cart.getItemsTotal();
        BigDecimal shipping   = cart.getShipping();

        // Strateji null değilse uygula
        if (strategy != null) {
            itemsTotal = strategy.applyTo(itemsTotal, shipping);
            shipping   = strategy.effectiveShipping(shipping);
        }

        BigDecimal grand = itemsTotal.add(shipping);

        Map<String, Object> out = new HashMap<>();
        out.put("itemsTotal", itemsTotal);
        out.put("shipping",   shipping);
        out.put("grandTotal", grand);

        if (strategy != null) {
            out.put("discountCode", strategy.code());
        }

        return out;
    }

    // ✅ Artık Factory üzerinden strateji üretimi
    public void applyDiscount(String code){
        strategy = DiscountFactory.fromCode(code);
        System.out.println("Uygulanan strateji: " + strategy.code());
    }

    public InMemoryProductRepo products(){ return repo; }
}
