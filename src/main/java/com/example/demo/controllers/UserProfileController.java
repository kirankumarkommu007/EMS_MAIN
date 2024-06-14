package com.example.demo.controllers;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Tag(name = "User Controller", description = "Controller for handling web page requests")
public class UserProfileController {

	private final EmployeeRepo employeeRepo;
	private final EmployeeServiceImpl employeeServiceImpl;

	public UserProfileController(EmployeeRepo employeeRepo, EmployeeServiceImpl employeeServiceImpl) {
		this.employeeRepo = employeeRepo;
		this.employeeServiceImpl = employeeServiceImpl;
	}

	@Operation(summary = "User profile", description = "Displays the user profile")
	@GetMapping("/profile")
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
	@GetMapping("/user/home")
	public String userDashboard() {
		return "/views/pages/userhome";
	}

	@GetMapping("/updateMyPassword")
	public String showUpdatePasswordForm(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String firstname = auth.getName(); // Assuming firstname is the username in this case

		Employees employee = employeeServiceImpl.findByFirstname(firstname);

		if (employee == null) {
			throw new RuntimeException("Employee not found");
		}

		model.addAttribute("employee", employee); // Pass the found employee

		return "/views/fragments/updateMyPassword";
	}

	@PostMapping("/updateMyPassword")
	public String updatePassword(Model model, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		Employees employee = employeeServiceImpl.findByFirstname(name);

		if (employee == null) {
			model.addAttribute("errorMessage", "Employee not found.");
			return "/views/fragments/updateMyPassword";
		}

		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("employee", employee);
			model.addAttribute("errorMessage", "Passwords do not match.");
			return "/views/fragments/updateMyPassword";
		}

		employeeServiceImpl.updatePassword(employee.getFirstname(), newPassword);
		model.addAttribute("successMessage", "Password updated successfully.");
		return "/views/fragments/updateMyPassword";
	}

}