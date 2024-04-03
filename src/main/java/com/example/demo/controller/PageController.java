package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class PageController {

//    @GetMapping("/{name}")
//    public String page(@PathVariable String name) {
//        return name; // 여기서 'name'은 src/main/resources/templates/ 내의 HTML 파일 이름입니다.
//    }
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/blog")
    public String blogPage() {
        return "blog";
    }

    @GetMapping("/blog2")
    public String blog2Page() {
        return "blog2";
    }

    @GetMapping("/blog-single")
    public String blogSinglePage() {
        return "blog-single";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/example")
    public String examplePage() {
        return "example";
    }

    @GetMapping("/footer")
    public String footerPage() {
        return "footer";
    }

    @GetMapping("/free")
    public String freePage() {
        return "free";
    }

    @GetMapping("/header")
    public String headerPage() {
        return "header";
    }

    @GetMapping("/header2")
    public String header2Page() {
        return "header2";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }


    @GetMapping("/product-single")
    public String productSinglePage() {
        return "product-single";
    }

    @GetMapping("/region")
    public String regionPage() {
        return "region";
    }

    @GetMapping("/register_success")
    public String registerSuccessPage() {
        return "register_success";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/shop")
    public String shopPage() {
        return "shop";
    }

    @GetMapping("/walk")
    public String walkPage() {
        return "walk"; 
    }
  
    @GetMapping("/wishlist") 
    public String wishlistPage() {
        return "wishlist";
    }

    @GetMapping("/qna")
    public String qnaPage() {
        return "qna";
    }
    
    @GetMapping("/write")
    public String writePage() {
        return "write";
    }
    
}
    
