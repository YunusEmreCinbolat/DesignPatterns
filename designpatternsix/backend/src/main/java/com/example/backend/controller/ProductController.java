package com.example.backend.controller;

import com.example.backend.flyweight.ProductFlyweightFactory;
import com.example.backend.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        Map<String, Product> cache = ProductFlyweightFactory.getAll();

        List<Product> list = new ArrayList<>(cache.values());

        System.out.println("[API] GET /api/products → returning " + list.size() + " products from Flyweight cache");

        return ResponseEntity.ok(list);
    }
}
