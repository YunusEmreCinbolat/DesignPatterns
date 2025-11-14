package com.example.backend.pricing;

import com.example.backend.product.ProductComponent;

/**
 * Proxy: Composite ürün yapısının üzerine sarılır ve
 * fiyat hesaplamasını cache'ler (lazy loading + cache).
 */
public class PriceCacheProxy implements ProductComponent {

    private final ProductComponent realProduct;
    private Double cachedPrice = null;

    public PriceCacheProxy(ProductComponent realProduct) {
        this.realProduct = realProduct;
    }

    @Override
    public String getName() {
        return realProduct.getName();
    }

    @Override
    public double getPrice() {
        if (cachedPrice == null) {
            System.out.println("[Proxy] Fiyat ilk kez hesaplanıyor...");
            cachedPrice = realProduct.getPrice();
        } else {
            System.out.println("[Proxy] Cached fiyat kullanılıyor...");
        }
        return cachedPrice;
    }

    @Override
    public void print() {
        realProduct.print();
    }

    public void invalidate() {
        cachedPrice = null;
    }
}
