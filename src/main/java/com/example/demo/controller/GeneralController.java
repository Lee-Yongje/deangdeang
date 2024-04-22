package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.dao.UsersDAO;
import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GeneralController {

	@Autowired
    private UsersDAO dao;
    
    @Autowired
    private UsersService userService;
	
    @GetMapping("/footer")
    public String footerPage() {
        return "footer";
    }

    @GetMapping("/header")
    public String headerPage() {
        return "header";
    }

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


}


//  @GetMapping("/index")    
//  public String processLogin(HttpSession session, Model model) {
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      
//      if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
//          org.springframework.security.core.userdetails.User userDetails = 
//              (org.springframework.security.core.userdetails.User) authentication.getPrincipal(); 
//          String username = userDetails.getUsername(); 
//          Users userSession = dao.findByEmail(username); 
//          if (userSession != null) {
//              session.setAttribute("userSession", userSession);
//              //model.addAttribute("userSession", userSession); 모델로 가져와야되면 주석해제해서 사용.
//          } else {
//              System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
//          }
//      } else {
//          System.out.println("principal 객체가 예상한 타입이 아닙니다.");
//      }
//      return "index";
//  }

//  @GetMapping("/index")
//  public String processLogin(HttpSession session, Model model) {
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      
//      if (authentication != null && authentication.isAuthenticated()) {
//          Users userSession = null;
//
//          if (authentication.getPrincipal() instanceof UserDetails) {
//              String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//              userSession = userService.findByEmail(username);
//          } else if (authentication.getPrincipal() instanceof Users) {
//              userSession = (Users) authentication.getPrincipal();
//          }
//
//          if (userSession != null) {
//              session.setAttribute("userSession", userSession);
//              model.addAttribute("userSession", userSession); // Uncomment if needed for the view
//          } else {
//              System.out.println("Database does not contain the user.");
//          }
//      } else {
//          System.out.println("No authentication information available or user is not authenticated.");
//      }
//      return "index";
//  }    
    
//    @GetMapping("/index")
//    public String processLogin(HttpSession session, Model model) {
//        Users userSession = (Users) session.getAttribute("userSession");
//        if (userSession != null) {
//            model.addAttribute("userSession", userSession);
//            System.out.println("User session retrieved successfully.");
//        } else {
//            System.out.println("No user session available.");
//        }
//        return "index";
//    }
    

    