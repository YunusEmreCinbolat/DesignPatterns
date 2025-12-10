package com.example.backend.builder;

import com.example.backend.dto.PizzaOrderRequest;
import com.example.backend.enums.DoughType;
import com.example.backend.enums.SauceType;
import com.example.backend.enums.Size;
import com.example.backend.model.Pizza;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PizzaBuilder {

    public Pizza buildPizza(PizzaOrderRequest request) {
        return Pizza.builder()
                .size(Size.valueOf(request.getSize()))
                .doughType(DoughType.valueOf(request.getDoughType()))
                .sauceType(SauceType.valueOf(request.getSauceType()))
                .spicy(request.isSpicy())
                .toppings(request.getToppings() != null ? request.getToppings() : new ArrayList<>())
                .build();
    }
}
