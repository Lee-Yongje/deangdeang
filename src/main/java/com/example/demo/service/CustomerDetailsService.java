package com.example.demo.service;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.entity.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomerDetailsService implements UserDetailsService {

	private final CustomerDAO customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerDetailsService(CustomerDAO customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new User(customer.getUsername(), customer.getPassword(), Collections.emptyList());
    }
	
	    
   
}