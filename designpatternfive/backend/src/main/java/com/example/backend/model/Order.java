package com.example.backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Order {

    private Long id;
    private String customerName;
    private Pizza pizza;
}
