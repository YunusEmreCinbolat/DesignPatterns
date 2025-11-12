package com.designpatternone.backend.domain.product;


import java.math.BigDecimal;
public class Book extends Product {

    private final String author;

    public Book(String id, String name, BigDecimal price, String author) {
        super(id, name, price); this.author = author;
    }

    public String getAuthor() { return author; }

    @Override
    public String getType() { return "BOOK"; }
}
