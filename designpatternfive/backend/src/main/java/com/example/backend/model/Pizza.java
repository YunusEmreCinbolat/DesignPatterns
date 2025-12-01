package com.example.backend.model;


import com.example.backend.enums.DoughType;
import com.example.backend.enums.SauceType;
import com.example.backend.enums.Size;

import java.util.ArrayList;
import java.util.Collections;
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
        this.toppings = Collections.unmodifiableList(builder.toppings);
        this.spicy = builder.spicy;
    }

    // --- GETTER'lar ---

    public Size getSize() {
        return size;
    }

    public DoughType getDoughType() {
        return doughType;
    }

    public SauceType getSauceType() {
        return sauceType;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public boolean isSpicy() {
        return spicy;
    }

    // --- Builder sınıfı ---

    public static class Builder {

        private Size size;
        private DoughType doughType;
        private SauceType sauceType;
        private List<String> toppings = new ArrayList<>();
        private boolean spicy;

        public Builder size(Size size) {
            this.size = size;
            return this;
        }

        public Builder doughType(DoughType doughType) {
            this.doughType = doughType;
            return this;
        }

        public Builder sauceType(SauceType sauceType) {
            this.sauceType = sauceType;
            return this;
        }

        public Builder addTopping(String topping) {
            this.toppings.add(topping);
            return this;
        }

        public Builder spicy(boolean spicy) {
            this.spicy = spicy;
            return this;
        }

        public Pizza build() {
            // basit bir validasyon örneği
            if (size == null) {
                throw new IllegalStateException("Pizza size must be set");
            }
            if (doughType == null) {
                doughType = DoughType.REGULAR;
            }
            if (sauceType == null) {
                sauceType = SauceType.TOMATO;
            }
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "size=" + size +
                ", doughType=" + doughType +
                ", sauceType=" + sauceType +
                ", toppings=" + toppings +
                ", spicy=" + spicy +
                '}';
    }
}
