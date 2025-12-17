package com.example.backend.controller;

import com.example.backend.dto.CartPriceRequest;
import com.example.backend.dto.CartPriceResponse;
import com.example.backend.service.CartPricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartPricingService cartPricingService;

    @PostMapping("/price")
    public ResponseEntity<CartPriceResponse> calculatePrice(@RequestBody CartPriceRequest request) {

        log.info("[API] POST /api/cart/price");
        return ResponseEntity.ok(cartPricingService.calculatePrice(request));
    }
}
