package com.example.backend.service;

import com.example.backend.dto.CartItemRequest;
import com.example.backend.dto.CartPriceRequest;
import com.example.backend.dto.CartPriceResponse;
import com.example.backend.facade.DiscountFacade;
import com.example.backend.flyweight.ProductFlyweightFactory;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.DiscountResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartPricingService {

    private final DiscountFacade discountFacade;

    public CartPriceResponse calculatePrice(CartPriceRequest request) {
        List<CartItemRequest> items = request.getItems() == null ? List.of() : request.getItems();

        log.info("[SERVICE] Calculating cart price → items={}, discountType={}", items.size(), request.getDiscountType());

        Cart cart = new Cart();
        for (CartItemRequest itemReq : items) {
            var product = ProductFlyweightFactory.getOrCreate(
                    itemReq.getProductId(),
                    itemReq.getName(),
                    itemReq.getPrice()
            );
            cart.addItem(new CartItem(product, itemReq.getQuantity()));
        }

        DiscountResult result = discountFacade.applyDiscount(cart, request.getDiscountType());

        return new CartPriceResponse(
                result.getSubtotal(),
                result.getDiscountAmount(),
                result.getTotalAfterDiscount(),
                result.getShippingFee(),
                result.getFinalTotal(),
                result.getDescription()
        );
    }
}
