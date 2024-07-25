package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    
    public List<Customer> getAllCustomers() {
        logger.info("Tum musteriler getiriliyor");
        return customerRepository.findAll();
    }

    
    public Optional<Customer> getCustomerById(Long id) {
        logger.info("Musteriler ID: {}", id);
        return customerRepository.findById(id);
    }

    
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            logger.error("Müşteri oluştururken hata: Email degeri bos olamaz");
            throw new IllegalArgumentException("Email degeri bos olamaz");
        }
        logger.info("Musteri email ile oluşturuldu: {}", customer.getEmail());
        return customerRepository.save(customer);
    }

    
    @Transactional
    public Customer updateCustomer(Long id, Customer customer) {
        if (customerRepository.existsById(id)) {
            customer.setId(id);
            logger.info("Musteri guncelleniyor ID: {}", id);
            return customerRepository.save(customer);
        }
        logger.warn("Musteri ID: {} update edilemedi", id);
        return null;
    }

    
    @Transactional
    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            logger.info("ID'li musteri siliniyor: {}", id);
            customerRepository.deleteById(id);
        } else {
            logger.warn("ID'li musteri: {} silmek icin bulunamadi", id);
        }
    }
}
