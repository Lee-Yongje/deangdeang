package com.example.demo.repository;

import com.example.demo.repository.CustomerRepository;

import jakarta.annotation.PostConstruct;

import com.example.demo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseConnectionTester { 

    @Autowired
    private CustomerRepository customerRepository;

    // 애플리케이션 시작 후 자동으로 실행될 메소드
    @PostConstruct
    public void testConnection() {
        // 예시로, id 값이 1인 Customer를 조회해 봅니다. 실제 데이터베이스에 맞게 조정해주세요.
        Optional<Customer> customer = customerRepository.findById(1L);
        
        if (customer.isPresent()) {
            System.out.println("Database connection successful. Found customer: " + customer.get().getUsername());
        } else {
            System.out.println("Database connection successful but no customer found with ID 1.");
        }
    }
}
