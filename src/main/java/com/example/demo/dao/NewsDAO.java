package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.News;

public interface NewsDAO extends JpaRepository<News, String> {

}
