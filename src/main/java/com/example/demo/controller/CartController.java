package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCartByCustomerId(@PathVariable Long customerId) {
        Cart cart = cartService.getCartByCustomerId(customerId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Cart> createCart(@PathVariable Long customerId) {
        Cart createdCart = cartService.createCart(customerId);
        return ResponseEntity.ok(createdCart);
    }

    @PostMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long customerId, @PathVariable Long productId, @RequestParam int quantity) {
        Cart cart = cartService.addProductToCart(customerId, productId, quantity);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long customerId, @PathVariable Long productId) {
        Cart cart = cartService.removeProductFromCart(customerId, productId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Cart> emptyCart(@PathVariable Long customerId) {
        Cart cart = cartService.emptyCart(customerId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return carts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carts);
    }
}
