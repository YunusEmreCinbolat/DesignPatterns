package com.example.backend.product;


public class SingleProduct implements ProductComponent {

    private final String id;
    private final String name;
    private final double price;

    public SingleProduct(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void print() {
        System.out.println("- " + name + " (" + id + ") : " + price + "₺");
    }
}
