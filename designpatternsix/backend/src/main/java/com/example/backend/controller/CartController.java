package com.example.backend.controller;

import com.example.backend.dto.CartItemRequest;
import com.example.backend.dto.CartPriceRequest;
import com.example.backend.dto.CartPriceResponse;
import com.example.backend.facade.DiscountFacade;
import com.example.backend.flyweight.ProductFlyweightFactory;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.DiscountResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final DiscountFacade discountFacade = new DiscountFacade();

    @PostMapping("/price")
    public ResponseEntity<CartPriceResponse> calculatePrice(@RequestBody CartPriceRequest request) {

        Cart cart = new Cart();

        for (CartItemRequest itemReq : request.getItems()) {
            var product = ProductFlyweightFactory.getOrCreate(
                    itemReq.getProductId(),
                    itemReq.getName(),
                    itemReq.getPrice()
            );
            cart.addItem(new CartItem(product, itemReq.getQuantity()));
        }

        DiscountResult result = discountFacade.applyDiscount(cart, request.getDiscountType());

        CartPriceResponse response = new CartPriceResponse(
                result.getSubtotal(),
                result.getDiscountAmount(),
                result.getFinalTotal(),
                result.getDescription()
        );

        return ResponseEntity.ok(response);
    }
}
