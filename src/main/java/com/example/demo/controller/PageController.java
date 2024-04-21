package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.dao.UsersDAO;
import com.example.demo.entity.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	

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

//    @GetMapping("/index")
//    public String indexPage() {
//        return "index";
//    }
    @Autowired
    private UsersDAO dao;
    
    @GetMapping("/index")    
    public String processLogin(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = 
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal(); 
            String username = userDetails.getUsername(); 
            Users userSession = dao.findByEmail(username); 
            if (userSession != null) {
                session.setAttribute("userSession", userSession);
                //model.addAttribute("userSession", userSession); 모델로 가져와야되면 주석해제해서 사용.
            } else {
                System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
            }
        } else {
            System.out.println("principal 객체가 예상한 타입이 아닙니다.");
        }
        return "index";
    }

    
    

    @GetMapping("/product-single")
    public String productSinglePage() {
        return "product-single";
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

    
   
}
    