package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.security.MyUserDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Tag(name = "Page Controller", description = "Controller for handling web page requests")
public class PageController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenProvider;

    @Autowired
    private MyUserDetailsService employeeDetailsService;

  

    @Operation(summary = "Welcome page", description = "Displays the welcome page")
    @GetMapping("/welcome")
    public String getWelcome() {
        return "welcome";
    }
    
    
    @Operation(summary = "add employee  page", description = "Displays the add employee form page")
    @GetMapping("/addempform")
    public String getform() {
        return "addempform";
    }

    @Operation(summary = "Handle login", description = "Processes login and sets JWT token in cookie")
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model,
            HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);
            String role = jwtTokenProvider.extractRoleAsString(token);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("token", token);
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("roles", role);
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "welcome";
        }
    }

    @Operation(summary = "Home page", description = "Displays the home page")
    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        String role = jwtTokenProvider.extractRoleAsString(token);
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roles", role);
        return "home";
    }


   
}
