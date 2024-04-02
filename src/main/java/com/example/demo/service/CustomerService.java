package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Customer;

@Service
public interface CustomerService {
    Customer findByUsername(String username);
}