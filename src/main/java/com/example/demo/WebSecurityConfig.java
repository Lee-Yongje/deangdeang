package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;

import com.example.demo.service.CustomOAuth2UserService;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.UsersService;


@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
    
    @SuppressWarnings("deprecation")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Permitting specific paths
        http.authorizeRequests(auth -> {
            auth.requestMatchers(
            		"/", "/registerMember", "/registerMember/**", "/login", "/index",
                    "/css/**", "/js/**", "/fonts/**", "/images/**", "/scss/**", "/data/**",
                    "/community/**", "/region/**", "/usedgood/**", "/auth/**", "/oauth2/**",
                    "/register", "/register_success", "/register_kakao", "/oauth2/authorization/kakao",
                    "/login/oauth2/code/kakao", "/news/**")
                .permitAll() // Allow these paths without authentication
            .requestMatchers("/member/usedgood/write")
            .authenticated(); // Require all requests to /member/usedgood/write to be authenticated
        });

        // Configuring form-based login
        http.formLogin(login -> {
            login.loginPage("/login")
                .defaultSuccessUrl("/index", true) // Redirect to /index upon successful login
                .failureUrl("/login?error=true") // Redirect back to login page with error flag
                .permitAll(); // Allow all users to access the login page
        });

        // Configuring logout behavior
        http.logout(logout -> {
            logout.logoutUrl("/logout") // URL to trigger logout
                .logoutSuccessUrl("/login?logout=true") // Redirect to login with a logout query parameter
                .invalidateHttpSession(true) // Invalidate session upon logout
                .deleteCookies("JSESSIONID") // Delete session cookies
                .permitAll(); // Allow all users to perform logout
        });

        // Configuring OAuth2 login
        http.oauth2Login(oauth -> {
            oauth.loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // Custom service for OAuth2 user details
                .and()
                .defaultSuccessUrl("/index", true) // Redirect to /index after OAuth2 login
                .failureUrl("/login?error=true") // Redirect to login on failure
                .permitAll(); // Allow all users to access OAuth2 login
        });

        // Configuring CSRF protection as per your existing requirements
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/login", "/logout", "/registerMember", "/register_kakao")); // Exclude login and logout actions from CSRF protection

        // Configuring exception handling for access denied
        http.exceptionHandling(exceptions -> exceptions.accessDeniedPage("/403"));

        return http.build(); // Build the security filter chain
    }
}