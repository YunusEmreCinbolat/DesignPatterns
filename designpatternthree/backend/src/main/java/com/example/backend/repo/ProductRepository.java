package com.example.backend.repo;

import com.example.backend.product.SingleProduct;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {

    private final Map<String, SingleProduct> store = new HashMap<>();

    @PostConstruct
    public void init() {
        save(new SingleProduct("P1", "Laptop", 25000));
        save(new SingleProduct("P2", "Mouse", 500));
        save(new SingleProduct("P3", "Kulaklık", 1200));
    }

    public void save(SingleProduct product) {
        store.put(product.getId(), product);
    }

    public SingleProduct findById(String id) {
        return store.get(id);
    }

    public Collection<SingleProduct> findAll() {
        return store.values();
    }
}
