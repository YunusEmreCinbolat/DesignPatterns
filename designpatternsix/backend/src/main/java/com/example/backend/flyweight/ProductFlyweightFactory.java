package com.example.backend.flyweight;

import com.example.backend.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductFlyweightFactory {

    private static final Map<String, Product> CACHE = new HashMap<>();

    public static Product getOrCreate(String id, String name, double price) {
        if (!CACHE.containsKey(id)) {
            System.out.println("[FLYWEIGHT] Cache MISS → creating shared Product instance (Flyweight) → " + id + " - " + name);
            CACHE.put(id, new Product(id, name, price));
        } else {
            System.out.println("[FLYWEIGHT] Cache HIT → reusing shared Product instance (Flyweight) → " + id);
        }
        return CACHE.get(id);
    }

    public static Map<String, Product> getAll() {
        System.out.println("[FLYWEIGHT] Returning all shared Product instances from cache → count=" + CACHE.size());
        return CACHE;
    }

    public static int cacheSize() {
        return CACHE.size();
    }
}
