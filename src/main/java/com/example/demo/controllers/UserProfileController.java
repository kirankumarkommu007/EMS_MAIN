package com.example.demo.controllers;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user_dashboard";
	}
	
	
//	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee role")
//	@GetMapping("/updatePassword")
//	public String updatePassword(@PathVariable Integer id, Model model) {
//		Optional<Employees> optionalEmp = employeeServiceImpl.getEmployeeById(id);
//		if (optionalEmp.isPresent()) {
//			model.addAttribute("Employee", optionalEmp.get());
//			return "/views/fragments/updatePassword";
//		} else {
//			return "redirect:/admin/home";
//		}
//	}
//
//	@Operation(summary = "Edit an existing employee role", description = "Processes the form submission to edit an existing employee role")
//	@PostMapping("/updatePassword/{id}")
//	public String updatePassword(@PathVariable Integer id, @RequestParam("password") String newpassword) {
//		employeeServiceImpl.updateEmployeePassword(id, newpassword);
//		return "redirect:/admin/home";
//	}
	
}