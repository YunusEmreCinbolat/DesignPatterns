package com.designpatternone.backend.controller;

// web/CartController.java
import com.designpatternone.backend.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public Object get(){
        return cartService.get();
    }

    @PostMapping("/add")
    public void add(@RequestParam String productId, @RequestParam(defaultValue="1") int qty){
        cartService.add(productId, qty);
    }

    @DeleteMapping("/remove/{productId}")
    public void rm(@PathVariable String productId){
        cartService.remove(productId);
    }

    @PostMapping("/clear")
    public void clear(){
        cartService.clear();
    }

    @GetMapping("/totals")
    public Object totals(){
        return cartService.totals();
    }

    @PostMapping("/discount")
    public void discount(@RequestParam String code){
        cartService.applyDiscount(code);
    }
}
