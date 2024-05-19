package com.example.demo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {

    private final EmployeeRepo employeeRepo;

    public UserProfileController(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("/user/profile")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firstName = authentication.getName();
        Employee user = employeeRepo.findByFirstname(firstName).orElse(null);
        if (user == null) {
           
            return "error";
        }

        model.addAttribute("Profile", user);

        return "profile";
    }
}
