package com.example.backend.controller;

import com.example.backend.product.Product;
import com.example.backend.product.SingleProduct;
import com.example.backend.repo.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Collection<SingleProduct> getAll() {
        return productRepository.findAll();
    }
}
