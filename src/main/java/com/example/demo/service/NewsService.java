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

    public List<News> crawlDataFromWebPage(int page, int size) {
        String url = "https://www.pet-news.or.kr/news/articleList.html?page=1&total=185&box_idxno=&sc_section_code=S1N45&view_type=sm";
        List<News> allNews = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsItems = document.select("li:has(.view-cont)");

            for (Element item : newsItems) {
                String title = item.select(".titles a").text();
                String link = "https://www.pet-news.or.kr" + item.select(".titles a").attr("href");
                String summary = item.select(".lead").text();
                String author = item.select(".byline em").get(1).text();
                String date = item.select(".byline em").get(2).text();
                String imageUrl = item.select(".thumb img").attr("src");

                News news = new News(title, link, summary, author, date, imageUrl);
                allNews.add(news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculating indices for pagination
        int start = Math.max((page - 1) * size, 0);
        int end = Math.min(start + size, allNews.size());
        return allNews.subList(start, end);
    }

    public class NewsData {
        private List<News> newsList;
        private int totalCount;

        public NewsData(List<News> newsList, int totalCount) {
            this.newsList = newsList;
            this.totalCount = totalCount;
        }

        public List<News> getNewsList() {
            return newsList;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }
    public int getTotalCount() {
        // As we do not store the data, we assume a static number or fetch dynamically every time
        return 20;  // Assuming a static total count as shown in the URL query parameter for simplicity
    }
}


