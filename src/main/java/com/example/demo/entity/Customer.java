package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno; // Assuming 'uno' is the primary key

    @Column(name = "u_name", length = 20, nullable = false)
    private String uName; // Matches 'u_name' in the database

    @Column(name = "u_email", length = 20)
    private String username; // Matches 'u_email' in the database

    @Column(name = "u_pwd", length = 20, nullable = false)
    private String password; // Matches 'u_pwd' in the database, but consider using a more secure name like 'passwordHash'

    @Column(name = "u_phone", length = 11)
    private String uPhone; // Matches 'u_phone' in the database

    @Column(name = "u_nickname", length = 20, nullable = false)
    private String uNickname; // Matches 'u_nickname' in the database

    @Column(name = "u_fname", length = 100)
    private String uFname; // Matches 'u_fname' in the database

    @Column(name = "rno", length = 10, nullable = false)
    private String rno; // Matches 'rno' in the database, assuming it's a foreign key

    // Getters and Setters

    // ...
}

