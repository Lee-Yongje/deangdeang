package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.News;

@Repository
public interface NewsDAO extends JpaRepository<News, String> {

}
