package com.example.backend.flyweight;

import com.example.backend.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductFlyweightFactory {

    private static final Map<String, Product> CACHE = new HashMap<>();

    public static Product getOrCreate(String id, String name, double price) {
        if (!CACHE.containsKey(id)) {
            System.out.println("[FLYWEIGHT] Creating new Product → " + id + " - " + name);
            CACHE.put(id, new Product(id, name, price));
        } else {
            System.out.println("[FLYWEIGHT] Reusing existing Product → " + id);
        }
        return CACHE.get(id);
    }

    public static int cacheSize() {
        return CACHE.size();
    }
}
