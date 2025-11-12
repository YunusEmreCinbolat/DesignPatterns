package com.designpatternone.backend.repo;

import com.designpatternone.backend.domain.product.Product;
import com.designpatternone.backend.domain.product.ProductType;
import com.designpatternone.backend.factory.ProductFactory;

import java.math.BigDecimal;
import java.util.*;

public class InMemoryProductRepo {
    private final Map<String, Product> store = new LinkedHashMap<>();

    public InMemoryProductRepo() {
        store.put("b1",
                ProductFactory.create(
                        ProductType.BOOK,
                        "b1",
                        "Clean Code",
                        new BigDecimal("399.90"),
                        Map.of("author", "Robert C. Martin")
                )
        );

        store.put("e1",
                ProductFactory.create(
                        ProductType.ELECTRONIC,
                        "e1",
                        "Kulaklık",
                        new BigDecimal("1299.00"),
                        Map.of("warrantyMonths", 24)
                )
        );

        store.put("c1",
                ProductFactory.create(
                        ProductType.CLOTHING,
                        "c1",
                        "T-Shirt",
                        new BigDecimal("199.90"),
                        Map.of("size", "L")
                )
        );
    }

    public Collection<Product> findAll() {
        return store.values();
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
