package com.example.demo.service;

import com.example.demo.entity.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    public List<News> crawlDataFromWebPage() {
        String url = "https://www.pet-news.or.kr/news/articleList.html?page=1&total=185&box_idxno=&sc_section_code=S1N45&view_type=sm";
        List<News> newsList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements newsItems = document.select("li:has(.view-cont)");

            for (Element item : newsItems) {
                String title = item.select(".titles a").text();
                String link = item.select(".titles a").attr("href");
                String summary = item.select(".lead").text();
                String author = item.select(".byline em").get(1).text(); // 저자 정보
                String date = item.select(".byline em").get(2).text(); // 날짜 정보
                String imageUrl = item.select(".thumb img").attr("src"); // 이미지 URL 추출

                
                
                // News 객체 생성 및 리스트에 추가
                News news = new News();
                news.setTitle(title);
                news.setLink(link);
                news.setSummary(summary);
                news.setAuthor(author);
                news.setDate(date);
                news.setImageUrl(imageUrl);
                newsList.add(news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}