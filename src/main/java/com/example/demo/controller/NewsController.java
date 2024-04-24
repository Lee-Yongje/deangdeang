package com.example.demo.controller;


import com.example.demo.entity.News;
import com.example.demo.service.NewsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news/news")
    public String listNews(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    	List<News> newsList = newsService.crawlDataFromWebPage(page, size);
        int totalItems = newsService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);

        return "/news/news";  // Assuming that 'news.html' is your Thymeleaf template
    }
}