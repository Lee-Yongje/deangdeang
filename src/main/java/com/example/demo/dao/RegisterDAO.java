package com.example.demo.dao;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterDAO extends JpaRepository<Users, Long> {
    // Here you can define custom database queries if necessary
}