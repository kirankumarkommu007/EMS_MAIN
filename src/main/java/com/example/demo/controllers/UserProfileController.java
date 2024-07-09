package com.example.demo.controllers;

import com.example.demo.models.Employees;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Tag(name = "User Controller", description = "Controller for handling web page requests")
public class UserProfileController {

	private final EmployeeServiceImpl employeeServiceImpl;
	public UserProfileController( EmployeeServiceImpl employeeServiceImpl) {
		this.employeeServiceImpl = employeeServiceImpl;
	}
	
	private static final String EMPLOYEE ="employee";

	@Operation(summary = "Employee Profile", description = "Displays the user profile")
	@GetMapping("/employeeprofile")
	public String userProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String employeeID = authentication.getName();
		Employees user = employeeServiceImpl.findByEmployeeId(employeeID);
		if (user == null) {
			return "error";
		}
		model.addAttribute("Profile", user);

		return "views/pages/profile";
	}

	@Operation(summary = "Employee dashboard", description = "Displays the user dashboard")
	@GetMapping("/user/home")
	public String userDashboard() {
		return "views/pages/userhome";
	}

	@GetMapping("/updateMyPassword")
	public String showUpdatePasswordForm(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String employeeid = auth.getName(); // Assuming firstname is the username in this case

		Employees employee = employeeServiceImpl.findByEmployeeId(employeeid);

		if (employee == null) {
			throw new RuntimeException("Employee not found");
		}

		model.addAttribute(EMPLOYEE, employee); // Pass the found employee

		return "views/fragments/updateMyPassword";
	}

	@Operation(summary = "Update Password for Logged User", description = "Update the password of present logged user")
	@PostMapping("/updateMyPassword")
	public String updatePassword(Model model,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmPassword") String confirmPassword) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
	    Employees employee = employeeServiceImpl.findByEmployeeId(name);

	    if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute(EMPLOYEE, employee);
	        model.addAttribute("errorMessage", "Passwords do not match.");
	        return "views/fragments/updateMyPassword";
	    }

	    // Update password
	    employeeServiceImpl.updatePassword(employee.getFirstname(), newPassword);

	    // Set success message as a flash attribute (for redirect)
	    model.addAttribute("successMessage", "Password updated successfully. Please Kindly Relogin");

	    // Update employee in model to display updated data on the same page
	    model.addAttribute(EMPLOYEE, employee);

	    // Return the same view with success message displayed
	    return "views/fragments/updateMyPassword";
	}
	
	
	
	
	@Operation(summary = "Search Employee", description = "Search the Employees By id")
	@GetMapping("/search")
	public String searchEmployeeById(@RequestParam("employeeId") String employeeId, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
	    boolean isAdmin = authentication.getAuthorities().stream()
	            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	    model.addAttribute("isAdmin", isAdmin);

	    Optional<Employees> employee = employeeServiceImpl.getEmployeeByEmployeeId(employeeId);
	    if (employee.isPresent()) {
	        model.addAttribute("Employee", employee.get());
	        return "views/pages/employeeslist"; // The view name to display the employee details
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "No Employee Found with Employeed id : "+employeeId+" Please Check the Employee ID");
	        return "redirect:/listemployees";
	    }
	}

	
	@GetMapping("/sidebar")
	public String getSimple() {
		return "views/fragments/sidebar";
	}
}