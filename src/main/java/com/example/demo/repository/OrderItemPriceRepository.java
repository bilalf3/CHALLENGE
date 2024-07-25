package com.example.demo.repository;

import com.example.demo.model.OrderItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemPriceRepository extends JpaRepository<OrderItemPrice, Long> {
}
