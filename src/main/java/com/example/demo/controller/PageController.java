package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/changeInfo")
    public String changeInfoPage() {
        return "changeInfo";
    }
    @GetMapping("/changePwd")
    public String changePwdPage() {
    	return "changePwd";
    }
    @GetMapping("/insertPuppy")
    public String insertPuppyPage() {
    	return "insertPuppy";
    }
    @GetMapping("/myPosts")
    public String myPostsPage() {
    	return "myPosts";
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login2")
    public String login2Page() {
        return "login2";
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
    
    @GetMapping("/scheduler")
    public String scheduler() {
        return "scheduler";
    } 
    @GetMapping("/schedulerWrite")
    public String schedulerWritePage() {
    	return "schedulerWrite";
    }
    
    @GetMapping("/diary")
    public String diaryPage() {
        return "diary";
    }
    @GetMapping("/diaryDetail")
    public String diaryDetailPage() {
    	return "diaryDetail";
    }
    @GetMapping("/diaryWrite")
    public String diaryWritePage() {
    	return "diaryWrite";
    }

    //사진형 게시판 boast, report, usedgood
    @GetMapping("/boast")
    public String boastPage() {
        return "boast";
    }
    @GetMapping("/report")
    public String reportPage() {
        return "report";
    }
    @GetMapping("/usedgood")
    public String usedgoodPage() {
        return "usedgood";
    }

    @GetMapping("/boastDetail")
    public String boastDetailPage() {
        return "boastDetail";
    }
    @GetMapping("/usedgoodDetail")
    public String usedgoodDetailPage() {
    	return "usedgoodDetail";
    }
    @GetMapping("/reportDetail")
    public String reportDetail() {
    	return "reportDetail";
    }
    
    @GetMapping("/boastWrite")
    public String boastWritePage() {
        return "boastWrite";
    }
    @GetMapping("/usedgoodWrite")
    public String usedgoodWritePage() {
    	return "usedgoodWrite";
    }
    @GetMapping("/reportWrite")
    public String reportWritePage() {
    	return "reportWrite";
    }
    //-사진형게시판
    
    //병원
    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    
    
    @GetMapping("/hospital")
    public String hospitalPage() {
    	return "hospital";
    }
    @GetMapping("/hospitalDetail")
    public String hospitalDetailPage(Model model) {
    	model.addAttribute("kakaoApiKey", kakaoApiKey);
    	return "hospitalDetail";
    }
    //-병원
    

}
    
