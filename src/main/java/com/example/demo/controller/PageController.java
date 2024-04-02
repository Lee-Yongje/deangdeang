package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/boast")
    public String boastPage() {
        return "boast";
    }

    @GetMapping("/boastDetail")
    public String boastDetailPage() {
        return "boastDetail";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/changeInfo")
    public String changeInfoPage() {
        return "changeInfo";
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

    @GetMapping("/report")
    public String reportPage() {
        return "report";
    }

    @GetMapping("/shop")
    public String shopPage() {
        return "shop";
    }

    @GetMapping("/usedgood")
    public String usedgoodPage() {
        return "usedgood";
    }

    @GetMapping("/walk")
    public String walkPage() {
        return "walk";
    }

    @GetMapping("/wishlist")
    public String wishlistPage() {
        return "wishlist";
    }

    @GetMapping("/write")
    public String writePage() {
        return "write";
    }
    
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/login")
    public String userLogin() {
        return "member/welcome";
    }
}
    
