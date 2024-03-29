//package com.example.demo.controller;
//
//import com.example.demo.entity.News;
//import com.example.demo.service.NewsService;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class NewsController {
//	@Autowired
//    private NewsService newsService;
//
//    @GetMapping("/news")
//    public String getNews(Model model) {
//        // NewsService에서 뉴스 데이터를 가져옵니다.
//        List<News> newsList = newsService.crawlDataFromWebPage();
//        
//        // 가져온 뉴스 데이터를 뷰 모델에 추가합니다.
//        model.addAttribute("newsList", newsList);
//        
//        // 뉴스를 보여줄 뷰의 이름을 반환합니다. (예: "News.html"의 Thymeleaf 템플릿)
//        return "News"; // Thymeleaf 템플릿 파일 이름 (확장자 제외)
//    }
//}