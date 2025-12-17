package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

    private final String id;
    private final String name;
    private final double price;
}


