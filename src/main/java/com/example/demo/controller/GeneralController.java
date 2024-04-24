package com.example.demo.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.example.demo.dao.UsersDAO;
import com.example.demo.entity.Users;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GeneralController {

	@Autowired
    private UsersDAO dao;
    
    @Autowired
    private UsersService userService;
	
    @Autowired
    private CustomUserDetailsService customUserService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
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
                System.out.println("로그인한 회원의 정보:" + userSession);

            } else {
                System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
            }
        } else {
            System.out.println("principal 객체가 예상한 타입이 아닙니다.");
        }
        return "index";
    }
    
//    @GetMapping("/index2")
//    public String processLogin2(Model model, HttpSession session) {
//        // SecurityContext에서 인증 객체를 가져옵니다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // 인증된 사용자의 세부사항을 가져옵니다.
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//            
//            // UserDetails로 캐스팅이 가능한 경우
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//                
//                // UserDetails에서 사용자 정보를 가져옵니다.
//                Users user = userService.findByEmail(userDetails.getUsername());
//                
//                // 세션에 사용자 정보를 저장합니다.
//                session.setAttribute("userSession", user);
//                
//                // 모델에 사용자 정보를 추가합니다.
//                model.addAttribute("user", user);
//                System.out.println("User " + user.getEmail() + " logged in successfully");
//            }
//        } else {
//            System.out.println("No authenticated user found");
//            return "redirect:/login";  // 사용자가 인증되지 않은 경우 로그인 페이지로 리다이렉트
//        }
//
//        return "index";  // index.html 페이지로 이동
//    }
    
  @GetMapping("/index2")
  public String processLogin2(HttpSession session, Model model) {
      Users userSession = (Users) session.getAttribute("userSession");
	  System.out.println(userSession);
	  String email = userSession.getEmail();
	  if (email != null && !email.trim().isEmpty()) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(email);
          if (userDetails != null) {
              Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(authentication);

              if (userSession != null) {
                  session.setAttribute("userSession", userSession);
                  model.addAttribute("userSession", userSession);
                  return "index";
              } else {
                  System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
                  return "news";
              }
          } else {
              System.out.println("User details not found for the email: " + email);
              return "login"; // Redirect to login if UserDetails is not found
          }
      } else {
          System.out.println("Email parameter is missing");
          return "login"; // Redirect to login if email is missing
      }
  }


// ***되는거 
//    @GetMapping("/index2")
//    public String processLogin(HttpSession session, Model model, @RequestParam(required = false) String email) {
//        if (email != null && !email.trim().isEmpty()) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//            if (userDetails != null) {
//                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                Users userSession = dao.findByEmail(email);
//                if (userSession != null) {
//                    session.setAttribute("userSession", userSession);
//                    model.addAttribute("user", userSession);
//                    return "index";
//                } else {
//                    System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
//                    return "news";
//                }
//            } else {
//                System.out.println("User details not found for the email: " + email);
//                return "login"; // Redirect to login if UserDetails is not found
//            }
//        } else {
//            System.out.println("Email parameter is missing");
//            return "login"; // Redirect to login if email is missing
//        }
//    }

    
//    @GetMapping("/indexKakao")
//    public String indexKakao(HttpSession session, Model model) {
//        // Retrieve the existingUser object from the session
//        Users user = (Users) session.getAttribute("existingUser");
//
//        // Also, check the authentication object to ensure the user is currently authenticated
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        
//        if (auth != null && auth.isAuthenticated() && user != null) {
//            // Check if the authenticated user matches the user in session
//            String emailFromAuth = ((UserDetails) auth.getPrincipal()).getUsername();
//            
//            if (user.getEmail().equals(emailFromAuth)) {
//                // Add user details to the model to be displayed in the view
//                model.addAttribute("user", user);
//
//                // Log for debugging purposes - to be removed or disabled in production
//                System.out.println("User logged in with email: " + user.getEmail());
//
//                // Redirect to the 'index' view that shows user-specific information
//                return "index";
//            } else {
//                System.out.println("Authentication mismatch, redirecting to login page.");
//                return "redirect:/login";
//            }
//        } else {
//            // If no user is found in the session or user is not authenticated, redirect to the login page
//            System.out.println("No user found in session or user not authenticated, redirecting to login page.");
//            return "redirect:/login";
//        }
//    }
//    
//    @GetMapping("/kakaoLogin")
//    public String kakaoLogin(HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        // Retrieve attributes from flash scope
//        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//        if (inputFlashMap != null) {
//            String kakaoUser = (String) inputFlashMap.get("kakaoUser");
//            String kakaoPass = (String) inputFlashMap.get("kakaoPass");
//            String email = (String) inputFlashMap.get("email");
//
//            // Perform login logic with these credentials
//            try {
//                authenticateUser(kakaoUser, kakaoPass);
//                redirectAttributes.addAttribute("email", email);
//                return "redirect:/indexKakao";
//            } catch (Exception e) {
//                return "login"; // Redirect to login page on failure
//            }
//        }
//        return "redirect:/login"; // Fallback if no attributes found
//    }
//
//    private void authenticateUser(String username, String password) {
//        // Assuming you have a custom method to authenticate and set the security context
//        UserDetails userDetails = customUserService.loadUserByUsername(username);
//        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
//    
    
    
    
    
//    @GetMapping("/index")    
//    public String processLogin(HttpSession session, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        
//        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
//            org.springframework.security.core.userdetails.User userDetails = 
//                (org.springframework.security.core.userdetails.User) authentication.getPrincipal(); 
//            String username = userDetails.getUsername(); 
//            Users userSession = dao.findByEmail(username); 
//            if (userSession != null) {
//                session.setAttribute("userSession", userSession);
//                //model.addAttribute("userSession", userSession); 모델로 가져와야되면 주석해제해서 사용.
//            } else {
//                System.out.println("데이터베이스에서 사용자를 찾을 수 없습니다.");
//            }
//        } else {
//            System.out.println("principal 객체가 예상한 타입이 아닙니다.");
//        }
//        return "index";
//    }


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
    

    
    