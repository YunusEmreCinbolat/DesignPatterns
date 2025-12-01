package com.example.backend.dto;


import java.util.List;

public class PizzaOrderRequest {

    private String customerName;
    private String size;        // SMALL / MEDIUM / LARGE
    private String doughType;   // THIN / REGULAR / THICK
    private String sauceType;   // TOMATO / BARBECUE / WHITE
    private List<String> toppings;
    private boolean spicy;

    public PizzaOrderRequest() {
    }

    // --- GETTER & SETTER'lar ---

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDoughType() {
        return doughType;
    }

    public void setDoughType(String doughType) {
        this.doughType = doughType;
    }

    public String getSauceType() {
        return sauceType;
    }

    public void setSauceType(String sauceType) {
        this.sauceType = sauceType;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public boolean isSpicy() {
        return spicy;
    }

    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }
}
