package com.example.demo;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class Soru5againApplication implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(Soru5againApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Ana menü:");
            System.out.println("1. Müşteri oluştur");
            System.out.println("2. Ürün oluştur");
            System.out.println("3. Sepete ürün ekle");
            System.out.println("4. Sepeti boşalt");
            System.out.println("5. Sipariş oluştur");
            System.out.println("6. Müşterileri listele");
            System.out.println("7. Ürünleri listele");
            System.out.println("q. Çıkış");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Müşteri oluştur
                    try {
                        System.out.println("Müşteri adı:");
                        String name = scanner.nextLine();
                        System.out.println("Müşteri email:");
                        String email = scanner.nextLine();

                        if (name.isEmpty() || email.isEmpty()) {
                            System.out.println("Geçersiz giriş. Müşteri adı ve email boş olamaz.");
                            break;
                        }

                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(email);
                        customer = customerService.createCustomer(customer);

                        System.out.println("Müşteri oluşturuldu: " + customer.getName());
                    } catch (Exception e) {
                        System.out.println("Bir hata oluştu: " + e.getMessage());
                    }
                    break;

                case "2":
                    // Ürün oluştur
                    try {
                        System.out.println("Ürün adı:");
                        String productName = scanner.nextLine();
                        System.out.println("Ürün fiyatı:");
                        BigDecimal price = scanner.nextBigDecimal();
                        System.out.println("Ürün stoğu:");
                        int stock = scanner.nextInt();
                        scanner.nextLine();  

                        if (productName.isEmpty() || price.compareTo(BigDecimal.ZERO) <= 0 || stock < 0) {
                            System.out.println("Geçersiz giriş. Ürün adı boş olamaz, fiyat sıfırdan büyük olmalı ve stok negatif olmamalıdır.");
                            break;
                        }

                        Product product = new Product();
                        product.setName(productName);
                        product.setPrice(price);
                        product.setStock(stock);
                        product = productService.createProduct(product);

                        System.out.println("Ürün oluşturuldu: " + product.getName());
                    } catch (InputMismatchException e) {
                        System.out.println("Geçersiz giriş. Lütfen doğru bir değer girin.");
                        scanner.nextLine();  
                    }
                    break;

                case "3":
                    // Sepete ürün ekle
                    try {
                        System.out.println("Müşteri ID:");
                        Long customerId = scanner.nextLong();
                        System.out.println("Ürün ID:");
                        Long productId = scanner.nextLong();
                        System.out.println("Miktar:");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();  

                        if (quantity <= 0) {
                            System.out.println("Geçersiz miktar. Miktar sıfırdan büyük olmalıdır.");
                            break;
                        }

                        Cart cart = cartService.addProductToCart(customerId, productId, quantity);
                        if (cart != null) {
                            System.out.println("Ürün sepete eklendi. Toplam fiyat: " + cart.getTotalPrice());
                        } else {
                            System.out.println("Sepet veya ürün bulunamadı.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Geçersiz giriş. Lütfen doğru bir değer girin.");
                        scanner.nextLine(); 
                    }
                    break;

                case "4":
                    // Sepeti boşalt
                    try {
                        System.out.println("Müşteri ID:");
                        Long emptyCartCustomerId = scanner.nextLong();
                        scanner.nextLine();  // Consume newline

                        Cart emptyCart = cartService.emptyCart(emptyCartCustomerId);
                        if (emptyCart != null) {
                            System.out.println("Sepet boşaltıldı.");
                        } else {
                            System.out.println("Sepet bulunamadı.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Geçersiz giriş. Lütfen doğru bir değer girin.");
                        scanner.nextLine(); 
                    }
                    break;

                case "5":
                    // Sipariş oluştur
                    try {
                        System.out.println("Müşteri ID:");
                        Long orderCustomerId = scanner.nextLong();
                        scanner.nextLine();  

                        Cart orderCart = cartService.getCartByCustomerId(orderCustomerId);
                        if (orderCart != null) {
                            // Sepet toplam fiyatını yazdır
                            System.out.println("Sepet toplam fiyatı: " + orderCart.getTotalPrice());

                            // Müşteri adını yazdır
                            Customer customer = orderCart.getCustomer();
                            if (customer != null) {
                                System.out.println("Müşteri adı: " + customer.getName());
                            } else {
                                System.out.println("Müşteri bulunamadı.");
                            }

                            // Siparişi oluştur
                            Order order = orderService.createOrderFromCart(orderCart);
                            System.out.println("Sipariş oluşturuldu: " + order.getId());
                        } else {
                            System.out.println("Sepet bulunamadı.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Geçersiz giriş. Lütfen doğru bir değer girin.");
                        scanner.nextLine();  
                    }
                    break;


                case "6":
                    // Müşterileri listele
                    try {
                        List<Customer> customers = customerService.getAllCustomers();
                        if (customers.isEmpty()) {
                            System.out.println("Hiç müşteri bulunamadı.");
                        } else {
                            System.out.println("Müşteriler:");
                            for (Customer c : customers) {
                                System.out.println("ID: " + c.getId() + ", Ad: " + c.getName() + ", Email: " + c.getEmail());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Bir hata oluştu: " + e.getMessage());
                    }
                    break;

                case "7":
                    // Ürünleri listele
                    try {
                        List<Product> products = productService.getAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("Hiç ürün bulunamadı.");
                        } else {
                            System.out.println("Ürünler:");
                            for (Product p : products) {
                                System.out.println("ID: " + p.getId() + ", Ad: " + p.getName() + ", Fiyat: " + p.getPrice() + ", Stok: " + p.getStock());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Bir hata oluştu: " + e.getMessage());
                    }
                    break;

                case "q":
                    // Çıkış
                    running = false;
                    System.out.println("Uygulama kapanıyor...");
                    break;

                default:
                    System.out.println("Geçersiz seçenek. Lütfen tekrar deneyin.");
                    break;
            }
        }
        scanner.close();
    }
}
