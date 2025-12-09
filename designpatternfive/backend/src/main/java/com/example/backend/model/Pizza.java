package com.example.backend.model;

import com.example.backend.enums.DoughType;
import com.example.backend.enums.SauceType;
import com.example.backend.enums.Size;

import java.util.ArrayList;
import java.util.List;

public class Pizza {

    private final Size size;
    private final DoughType doughType;
    private final SauceType sauceType;
    private final List<String> toppings;
    private final boolean spicy;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.doughType = builder.doughType;
        this.sauceType = builder.sauceType;
        this.toppings = builder.toppings;
        this.spicy = builder.spicy;

        System.out.println("[BUILDER] Pizza created → "
                + "size=" + size
                + ", dough=" + doughType
                + ", sauce=" + sauceType
                + ", toppings=" + toppings
                + ", spicy=" + spicy);
    }

    public Size getSize() { return size; }
    public DoughType getDoughType() { return doughType; }
    public SauceType getSauceType() { return sauceType; }
    public List<String> getToppings() { return toppings; }
    public boolean isSpicy() { return spicy; }

    public static class Builder {

        private Size size;
        private DoughType doughType;
        private SauceType sauceType;
        private List<String> toppings = new ArrayList<>();
        private boolean spicy;

        public Builder size(Size size) {
            System.out.println("[BUILDER] Setting size → " + size);
            this.size = size;
            return this;
        }

        public Builder doughType(DoughType doughType) {
            System.out.println("[BUILDER] Setting dough type → " + doughType);
            this.doughType = doughType;
            return this;
        }

        public Builder sauceType(SauceType sauceType) {
            System.out.println("[BUILDER] Setting sauce type → " + sauceType);
            this.sauceType = sauceType;
            return this;
        }

        public Builder addTopping(String topping) {
            System.out.println("[BUILDER] Adding topping → " + topping);
            this.toppings.add(topping);
            return this;
        }

        public Builder spicy(boolean spicy) {
            System.out.println("[BUILDER] Setting spicy → " + spicy);
            this.spicy = spicy;
            return this;
        }

        public Pizza build() {
            System.out.println("[BUILDER] Building final Pizza object…");
            return new Pizza(this);
        }
    }
}
