package com.example.backend.model;

import com.example.backend.enums.DoughType;
import com.example.backend.enums.SauceType;
import com.example.backend.enums.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
public class Pizza {

    private Size size;
    private DoughType doughType;
    private SauceType sauceType;

    @Builder.Default
    private List<String> toppings = new ArrayList<>();

    @Builder.Default
    private boolean spicy = false;
}
