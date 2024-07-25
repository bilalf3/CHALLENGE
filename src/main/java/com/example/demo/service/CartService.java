package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository; 

    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    } 

    public Cart createCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomer(customerRepository.findById(customerId).orElse(null));
        cart.setTotalPrice(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    public Cart addProductToCart(Long customerId, Long productId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);

        if (cart == null) {
            cart = createCart(customerId);
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Stok kontrolü
            if (product.getStock() < quantity) {
                throw new RuntimeException("Ürün stoğu yetersiz: " + product.getName());
            }

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (existingCartItem.isPresent()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
                cart.getItems().add(cartItem); // Yeni item'i cart'a ekle
            }

            // Ürün stoğunu güncelle
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            updateCartTotalPrice(cart);
            return cartRepository.save(cart);
        }
        return null;
    }

    public Cart removeProductFromCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        if (cart != null) {
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(cart, product);
                if (optionalCartItem.isPresent()) {
                    CartItem cartItem = optionalCartItem.get();
                    cartItemRepository.delete(cartItem);

                    // Stok güncelleme
                    product.setStock(product.getStock() + cartItem.getQuantity());
                    productRepository.save(product);

                    cart.getItems().remove(cartItem); // Item'i cart'tan çıkar
                    updateCartTotalPrice(cart);
                    return cartRepository.save(cart);
                }
            }
        }
        return null;
    }

    public Cart emptyCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        if (cart != null) {
            cartItemRepository.deleteAllByCart(cart);
            cart.setTotalPrice(BigDecimal.ZERO);
            cart.getItems().clear(); // Cart'taki tüm item'ları temizle
            return cartRepository.save(cart);
        }
        return null;
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    private void updateCartTotalPrice(Cart cart) {
        if (cart.getItems() != null) {
            BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalPrice(totalPrice);
        } else {
            cart.setTotalPrice(BigDecimal.ZERO);
        }
    }
}
