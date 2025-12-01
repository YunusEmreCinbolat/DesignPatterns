package com.example.backend.model;


public class Order {

    private final long id;
    private final String customerName;
    private final Pizza pizza;

    public Order(long id, String customerName, Pizza pizza) {
        this.id = id;
        this.customerName = customerName;
        this.pizza = pizza;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Pizza getPizza() {
        return pizza;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", pizza=" + pizza +
                '}';
    }
}
