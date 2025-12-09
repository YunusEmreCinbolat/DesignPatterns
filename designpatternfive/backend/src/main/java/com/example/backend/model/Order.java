package com.example.backend.model;

public class Order {

    private final int id;
    private final String customerName;
    private final Pizza pizza;

    public Order(int id, String customerName, Pizza pizza) {
        this.id = id;
        this.customerName = customerName;
        this.pizza = pizza;

        System.out.println("[ORDER] Created → id=" + id + ", customer=" + customerName);
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public Pizza getPizza() { return pizza; }

    @Override
    public String toString() {
        return "Order(id=" + id + ", customer=" + customerName + ", pizza=" + pizza + ")";
    }
}
