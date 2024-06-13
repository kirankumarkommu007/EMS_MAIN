package com.example.demo.controllers;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "User Controller", description = "Controller for handling web page requests")
public class UserProfileController {

	private final EmployeeRepo employeeRepo;

	public UserProfileController(EmployeeRepo employeeRepo) {
		this.employeeRepo = employeeRepo;
	}

    @Operation(summary = "User profile", description = "Displays the user profile")
	@GetMapping("/user/profile")
	public String userProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String firstName = authentication.getName();
		Employees user = employeeRepo.findByFirstname(firstName);
		if (user == null) {
			return "error";
		}

		model.addAttribute("Profile", user);

		return "profile";
	}

	@Operation(summary = "User dashboard", description = "Displays the user dashboard")
	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user_dashboard";
	}
}