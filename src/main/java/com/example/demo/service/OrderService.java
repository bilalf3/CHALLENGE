package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderItemPrice;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderItemPriceRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemPriceRepository orderItemPriceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        // Toplam ücret hesaplama
        BigDecimal totalPrice = order.getItems().stream()
            .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalPrice(totalPrice);

        // Siparişin kaydedilmesi
        Order savedOrder = orderRepository.save(order);

        // OrderItemPrice tablolarının güncellenmesi
        order.getItems().forEach(item -> {
            OrderItemPrice priceRecord = new OrderItemPrice();
            priceRecord.setOrderItem(item);
            priceRecord.setPriceAtOrderTime(item.getPriceAtPurchase());
            priceRecord.setQuantity(item.getQuantity());
            orderItemPriceRepository.save(priceRecord);
        });

        // Stok güncelleme
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Ürün stoğu yetersiz: " + product.getName());
            }
            product.setStock(newStock);
            productRepository.save(product);
        });

        return savedOrder;
    }

    @Transactional
    public Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setCustomer(cart.getCustomer());

        // Sepet öğelerini sipariş öğelerine dönüştürme
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice()); // Fiyatı al ve ayarla
            return orderItem;
        }).collect(Collectors.toList());

        // Toplam ücret hesaplama
        BigDecimal totalPrice = orderItems.stream()
            .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalPrice(totalPrice);
        order.setItems(orderItems);

        // Siparişi ve sipariş öğelerini kaydetme
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // OrderItemPrice tablolarının güncellenmesi
        orderItems.forEach(item -> {
            OrderItemPrice priceRecord = new OrderItemPrice();
            priceRecord.setOrderItem(item);
            priceRecord.setPriceAtOrderTime(item.getPriceAtPurchase());
            priceRecord.setQuantity(item.getQuantity());
            orderItemPriceRepository.save(priceRecord);
        });

        // Stok güncelleme
        cart.getItems().forEach(cartItem -> {
            Product product = cartItem.getProduct();
            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Ürün stoğu yetersiz: " + product.getName());
            }
            product.setStock(newStock);
            productRepository.save(product);
        });

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
