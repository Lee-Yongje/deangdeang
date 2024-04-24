package com.example.demo.dao;

import com.example.demo.entity.News;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDAO extends PagingAndSortingRepository<News, String> {
    // This interface now uses DAO naming convention and extends PagingAndSortingRepository
}
