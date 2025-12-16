package com.example.backend.config;

import com.example.backend.flyweight.ProductFlyweightFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductCatalogConfig {

    @PostConstruct
    public void preloadProducts() {

        System.out.println("[INIT] Preloading 10 products into Flyweight Cache");

        ProductFlyweightFactory.getOrCreate("P1", "Samsung TV", 1200);
        ProductFlyweightFactory.getOrCreate("P2", "iPhone 15", 999);
        ProductFlyweightFactory.getOrCreate("P3", "Lenovo Laptop", 850);
        ProductFlyweightFactory.getOrCreate("P4", "Sony Headphones", 199);
        ProductFlyweightFactory.getOrCreate("P5", "Logitech Mouse", 49);
        ProductFlyweightFactory.getOrCreate("P6", "Mechanical Keyboard", 139);
        ProductFlyweightFactory.getOrCreate("P7", "Smart Watch", 180);
        ProductFlyweightFactory.getOrCreate("P8", "External SSD", 120);
        ProductFlyweightFactory.getOrCreate("P9", "Gaming Chair", 350);
        ProductFlyweightFactory.getOrCreate("P10", "Desk Lamp", 39);

        System.out.println("[INIT] Loaded Product Count → " + ProductFlyweightFactory.cacheSize());
    }
}
