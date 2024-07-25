package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id") // Veritabanı ilişkisi için müşteri ID'si
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>(); // collect,on başlatma

    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // Getter ve Setter metodları
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        updateTotalPrice(); // collection değiştirildiğinde toplam fiyatı güncelle
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Toplam fiyatı güncelleyen yardımcı fonksiyon
    public void updateTotalPrice() {
        if (items != null) {
            this.totalPrice = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        updateTotalPrice();
    }

    
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }
}
