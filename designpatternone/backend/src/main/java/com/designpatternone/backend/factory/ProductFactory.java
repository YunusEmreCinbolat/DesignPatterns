package com.designpatternone.backend.factory;

import com.designpatternone.backend.domain.product.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ProductFactory
 * Factory Method Pattern + Enum kullanımı.
 *
 * Enum sayesinde yanlış string girilme hataları önlenir.
 */
public class ProductFactory {

    public static Product create(ProductType type, String id, String name, BigDecimal price, Map<String, Object> extra) {

        if (type == null) {
            throw new IllegalArgumentException("ProductType cannot be null");
        }

        return switch (type) {
            case BOOK -> new Book(
                    id, name, price,
                    (String) extra.getOrDefault("author", "Unknown")
            );

            case ELECTRONIC -> new Electronic(
                    id, name, price,
                    (int) extra.getOrDefault("warrantyMonths", 24)
            );

            case CLOTHING -> new Clothing(
                    id, name, price,
                    (String) extra.getOrDefault("size", "M")
            );
        };
    }
}
