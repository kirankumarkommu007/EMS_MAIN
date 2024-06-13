package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.models.Employees;
import com.example.demo.models.PasswordForm;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.security.MyUserDetailsService;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Tag(name = "Operations Controller", description = "Controller for handling web page requests")
public class OperationsControllers {

	private final EmployeeServiceImpl employeeService;

	public OperationsControllers(EmployeeServiceImpl employeeService) {
		this.employeeService = employeeService;
	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenProvider;

	@Autowired
	private MyUserDetailsService employeeDetailsService;

	@Operation(summary = "Welcome page", description = "Displays the welcome page")
	@GetMapping("/welcome")
	public String getWelcome() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/home";
		}
		return "welcome";
	}

	@Operation(summary = "add employee  page", description = "Displays the add employee form page")
	@GetMapping("/addempform")
	public String getform() {
		return "addempform";
	}

	@Operation(summary = "Handle login", description = "Processes login and sets JWT token in cookie")
	@PostMapping("/home")
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

	@GetMapping("/listemployees")
	public String EmployeesList(Model model) {
		List<Employees> empList = employeeService.getAllEmployees();
		model.addAttribute("Employee", empList);
		return "/views/pages/employeeslist";
	}

	@Operation(summary = "Show add employee form", description = "Displays the form for adding a new employee")
	@GetMapping("/addemployees")
	public String AddEmpForm(Model model) {
		model.addAttribute("Employee", new Employees());
		return "/views/fragments/addempform";
	}

	@PostMapping("/addemployees")
//	public String addEmp(@ModelAttribute("Employee") Employees employee) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String firstName = authentication.getName();
//		Employees user = employeeRepo.findByFirstname(firstName);
//
//		// Encode the password (assuming mobile is the password)
//		String encodedPassword = encoder.encode(employee.getMothername());
//		System.out.println(employee.getFirstname());
//		employee.setPassword(encodedPassword);
//
//		// Add the employee
//		employeeService.addEmployee(employee);
//
//		return "redirect:/admin/home";
//	}
	
	public String addEmp(@ModelAttribute("Employee") Employees employee) {
   
        employeeService.addEmployee(employee);

        return "redirect:/admin/home"; // Redirect to the admin home page
    }

	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee")
	@GetMapping("/edit/{id}")
	public String editEmpForm(@PathVariable Integer id, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeById(id);
		if (optionalEmp.isPresent()) {
			model.addAttribute("Employee", optionalEmp.get());
			return "/views/fragments/editemp";
		} else {
			return "redirect:/admin/home";
		}
	}

	@Operation(summary = "Edit an existing employee", description = "Processes the form submission to edit an existing employee")
	@PostMapping("/edit/{id}")
	public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employees employee) {
		employeeService.updateEmployee(id, employee);
		return "redirect:/admin/home";
	}

	@Operation(summary = "Delete an employee", description = "Deletes an existing employee")
	@GetMapping("/delete/{id}")
	public String deleteEmp(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
		return "redirect:/listemployees";
	}
	
	@GetMapping("/updatePassword")
    public String updatePasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "/views/fragments/updatePassword"; // return the name of your Thymeleaf template
    }

    @PostMapping("/updatePassword")
    public String updatePasswordSubmit(@ModelAttribute("passwordForm") PasswordForm passwordForm) {
        String firstname = passwordForm.getFirstname();
        String email = passwordForm.getEmail();
        String newPassword = passwordForm.getNewPassword();
        String confirmPassword = passwordForm.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            // Passwords do not match, handle this case
            return "redirect:/updatePassword?error=Passwords+do+not+match";
        }

        Employees updatedEmployee = employeeService.updatePassword(firstname, email, newPassword);

        if (updatedEmployee != null) {
            return "redirect:/Home"; // Replace with your success page URL
        } else {

            return "redirect:/error"; // Replace with your error page URL
        }
    }

}
