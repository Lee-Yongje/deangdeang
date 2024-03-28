package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/{name}")
    public String page(@PathVariable String name) {
        return name; // 여기서 'name'은 src/main/resources/templates/ 내의 HTML 파일 이름입니다.
    }
    
}
