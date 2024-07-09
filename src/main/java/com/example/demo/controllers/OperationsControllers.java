package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.models.Employees;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@Tag(name = "Operations Controller", description = "Controller for handling web page requests")
public class OperationsControllers {

	private final EmployeeServiceImpl employeeService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtTokenProvider;

	private static final Logger logger = LoggerFactory.getLogger(OperationsControllers.class);

	public OperationsControllers(EmployeeServiceImpl employeeService, AuthenticationManager authenticationManager,
			JwtUtil jwtTokenProvide) {
		this.employeeService = employeeService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvide;

	}

	public static final String EMPLOYEE = "Employee";
	private static final String REDIRECT_LIST_EMPLOYEES = "redirect:/listemployees";
	private static final String REDIRECT_WELCOME = "redirect:/welcome";
	private static final String SUCCESS_MESSAGE = "successMessage";

    private static final String ERROR_EMPLOYEE ="error.employee";



	@Operation(summary = "Welcome page", description = "Displays the welcome page")
	@GetMapping("/welcome")
	public String getWelcome() {
        logger.info("Accessing welcome page");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			String role = authentication.getAuthorities().iterator().next().getAuthority();

			if (role.contains("ROLE_ADMIN")) {
				return "redirect:/admin/home";
			} else if (role.contains("ROLE_HR")) {
				return "redirect:/hr/home";
			} else if (role.contains("ROLE_USER")) {
				return "redirect:/user/home";
			}
		}
		return "views/pages/welcome";
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
			return REDIRECT_WELCOME;

		} catch (Exception e) {
			model.addAttribute("error", "Invalid username or password ");
			return "views/pages/welcome";
		}
	}

	@GetMapping("/listemployees")
	public String employeesList(Model model, Authentication authentication) {

		List<Employees> empList = employeeService.getAllEmployees();
		model.addAttribute(EMPLOYEE, empList);

//		if (isAdmin) {
//			empList = employeeService.getAllEmployees();
//		} else {
//			empList = employeeService.getAllEmployeesExceptAdmin();
//		}
//		model.addAttribute("isAdmin", isAdmin);

		return "views/pages/employeeslist";
	}

	@Operation(summary = "Show add employee form", description = "Displays the form for adding a new employee")
	@GetMapping("/addemployees")
	public String addEmpForm(Model model) {
		model.addAttribute(EMPLOYEE, new Employees());
		return "views/fragments/addempform";
	}

	@PostMapping("/addemployees")
	public String addEmp(@Valid @ModelAttribute(EMPLOYEE) Employees employee, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "views/fragments/addempform"; // Return to form with validation errors
		}

		try {
			employeeService.addEmployee(employee);
			redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Employee added successfully");
		} catch (Exception e) {
			if (employeeService.existsByAdhaar(employee.getAdhaar())) {
				bindingResult.rejectValue("adhaar", ERROR_EMPLOYEE, "Aadhar number already exists");
			}
			if (employeeService.existsByPan(employee.getPan())) {
				bindingResult.rejectValue("pan", ERROR_EMPLOYEE, "PAN number already exists");
			}
			if (employeeService.existsByMobile(employee.getMobile())) {
				bindingResult.rejectValue("mobile", ERROR_EMPLOYEE, "Mobile number already exists");
			}
			if (employeeService.existsByEmail(employee.getEmail())) {
				bindingResult.rejectValue("email", ERROR_EMPLOYEE, "Email already exists");
			}
			return "views/fragments/addempform"; // Return to form with error messages
		}

		return "redirect:/addemployees"; // Redirect to a different endpoint after successful addition
	}

	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee")
	@GetMapping("/edit/{employeeid}")
	public String editEmpForm(@PathVariable String employeeid, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);
		if (optionalEmp.isPresent()) {
			model.addAttribute(EMPLOYEE, optionalEmp.get());
			return "views/fragments/editemp";
		} else {
			return "redirect:/admin/home";
		}
	}

	@Operation(summary = "Edit an existing employee", description = "Processes the form submission to edit an existing employee")
	@PostMapping("/edit/{employeeid}")
	public String editEmployees(@PathVariable String employeeid, @ModelAttribute(EMPLOYEE) Employees updatedEmployee,
			RedirectAttributes attributes) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);
		if (optionalEmp.isPresent()) {
			Employees existingEmployee = optionalEmp.get();

			// Update only the fields that are provided in the form
			if (updatedEmployee.getFirstname() != null) {
				existingEmployee.setFirstname(updatedEmployee.getFirstname());
			}
			if (updatedEmployee.getMiddlename() != null) {
				existingEmployee.setMiddlename(updatedEmployee.getMiddlename());
			}
			if (updatedEmployee.getLastname() != null) {
				existingEmployee.setLastname(updatedEmployee.getLastname());
			}
			if (updatedEmployee.getDateOfBirth() != null) {
				existingEmployee.setDateOfBirth(updatedEmployee.getDateOfBirth());
			}
			if (updatedEmployee.getGender() != null) {
				existingEmployee.setGender(updatedEmployee.getGender());
			}
			if (updatedEmployee.getBloodgroup() != null) {
				existingEmployee.setBloodgroup(updatedEmployee.getBloodgroup());
			}
			if (updatedEmployee.getMobile() != null) {
				existingEmployee.setMobile(updatedEmployee.getMobile());
			}
			if (updatedEmployee.getEmail() != null) {
				existingEmployee.setEmail(updatedEmployee.getEmail());
			}
			if (updatedEmployee.getPan() != null) {
				existingEmployee.setPan(updatedEmployee.getPan());
			}
			if (updatedEmployee.getAdhaar() != null) {
				existingEmployee.setAdhaar(updatedEmployee.getAdhaar());
			}
			if (updatedEmployee.getFathername() != null) {
				existingEmployee.setFathername(updatedEmployee.getFathername());
			}
			if (updatedEmployee.getMothername() != null) {
				existingEmployee.setMothername(updatedEmployee.getMothername());
			}
			if (updatedEmployee.getMaritalStatus() != null) {
				existingEmployee.setMaritalStatus(updatedEmployee.getMaritalStatus());
			}
			if (updatedEmployee.getSpousename() != null) {
				existingEmployee.setSpousename(updatedEmployee.getSpousename());
			}
			if (updatedEmployee.getPermanentaddress() != null) {
				existingEmployee.setPermanentaddress(updatedEmployee.getPermanentaddress());
			}
			if (updatedEmployee.getCommunicationaddress() != null) {
				existingEmployee.setCommunicationaddress(updatedEmployee.getCommunicationaddress());
			}
			if (updatedEmployee.getHighestqualification() != null) {
				existingEmployee.setHighestqualification(updatedEmployee.getHighestqualification());
			}
			if (updatedEmployee.getQualifyingbranch() != null) {
				existingEmployee.setQualifyingbranch(updatedEmployee.getQualifyingbranch());
			}
			if (updatedEmployee.getYearOfPassing() != null) {
				existingEmployee.setYearOfPassing(updatedEmployee.getYearOfPassing());
			}
			if (updatedEmployee.getUniversity() != null) {
				existingEmployee.setUniversity(updatedEmployee.getUniversity());
			}
			if (updatedEmployee.getCollegeaddress() != null) {
				existingEmployee.setCollegeaddress(updatedEmployee.getCollegeaddress());
			}
			if (updatedEmployee.getCgpaPercentage() != null) {
				existingEmployee.setCgpaPercentage(updatedEmployee.getCgpaPercentage());
			}
			if (updatedEmployee.getTechnicalCertification() != null) {
				existingEmployee.setTechnicalCertification(updatedEmployee.getTechnicalCertification());
			}
			if (updatedEmployee.getTechnicalSkills() != null) {
				existingEmployee.setTechnicalSkills(updatedEmployee.getTechnicalSkills());
			}
			if (updatedEmployee.getDepartment() != null) {
				existingEmployee.setDepartment(updatedEmployee.getDepartment());
			}
			if (updatedEmployee.getManagerId() != null) {
				existingEmployee.setManagerId(updatedEmployee.getManagerId());
			}
			if (updatedEmployee.getSalary() != null) {
				existingEmployee.setSalary(updatedEmployee.getSalary());
			}
			if (updatedEmployee.getDesignation() != null) {
				existingEmployee.setDesignation(updatedEmployee.getDesignation());
			}
			if (updatedEmployee.getDateOfJoining() != null) {
				existingEmployee.setDateOfJoining(updatedEmployee.getDateOfJoining());
			}
			if (updatedEmployee.getDateOfLeaving() != null) {
				existingEmployee.setDateOfLeaving(updatedEmployee.getDateOfLeaving());
			}
			if (updatedEmployee.getYearsexperience() != null) {
				existingEmployee.setYearsexperience(updatedEmployee.getYearsexperience());
			}
			if (updatedEmployee.getJobRole() != null) {
				existingEmployee.setJobRole(updatedEmployee.getJobRole());
			}
			if (updatedEmployee.getPreviousCompany() != null) {
				existingEmployee.setPreviousCompany(updatedEmployee.getPreviousCompany());
			}
			if (updatedEmployee.getUanNumber() != null) {
				existingEmployee.setUanNumber(updatedEmployee.getUanNumber());
			}
			if (updatedEmployee.getDateOfLeavingCompany() != null) {
				existingEmployee.setDateOfLeavingCompany(updatedEmployee.getDateOfLeavingCompany());
			}
			if (updatedEmployee.getEmergencyContactPerson1() != null) {
				existingEmployee.setEmergencyContactPerson1(updatedEmployee.getEmergencyContactPerson1());
			}
			if (updatedEmployee.getEmergencyContactPerson1mobile() != null) {
				existingEmployee.setEmergencyContactPerson1mobile(updatedEmployee.getEmergencyContactPerson1mobile());
			}
			if (updatedEmployee.getEmergencyContactPerson1email() != null) {
				existingEmployee.setEmergencyContactPerson1email(updatedEmployee.getEmergencyContactPerson1email());
			}
			if (updatedEmployee.getEmergencyContactPerson1relation() != null) {
				existingEmployee
						.setEmergencyContactPerson1relation(updatedEmployee.getEmergencyContactPerson1relation());
			}
			if (updatedEmployee.getEmergencyContactPerson2() != null) {
				existingEmployee.setEmergencyContactPerson2(updatedEmployee.getEmergencyContactPerson2());
			}
			if (updatedEmployee.getEmergencyContactPerson2mobile() != null) {
				existingEmployee.setEmergencyContactPerson2mobile(updatedEmployee.getEmergencyContactPerson2mobile());
			}
			if (updatedEmployee.getEmergencyContactPerson2email() != null) {
				existingEmployee.setEmergencyContactPerson2email(updatedEmployee.getEmergencyContactPerson2email());
			}
			if (updatedEmployee.getEmergencyContactPerson2relation() != null) {
				existingEmployee
						.setEmergencyContactPerson2relation(updatedEmployee.getEmergencyContactPerson2relation());
			}

			employeeService.updateEmployee(employeeid, existingEmployee);
			attributes.addFlashAttribute(SUCCESS_MESSAGE,
					"Employee with ID " + employeeid + " has been updated successfully.");

		}
		return REDIRECT_LIST_EMPLOYEES;

	}

	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee role")
	@GetMapping("/updateRole/{employeeid}")
	public String updateRole(@PathVariable String employeeid, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);
		if (optionalEmp.isPresent()) {
			model.addAttribute(EMPLOYEE, optionalEmp.get());
			return "views/fragments/updateRole";
		} else {
			return REDIRECT_WELCOME;
		}
	}

	@Operation(summary = "Edit an existing employee role", description = "Processes the form submission to edit an existing employee role")
	@PostMapping("/updateRole/{employeeid}")
	public String updateRole(@PathVariable String employeeid, @RequestParam("role") String role,
			RedirectAttributes redirectAttributes) {
		employeeService.updateEmployeeRole(employeeid, role);
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE,
				"Role updated successfully for employee with ID " + employeeid + ".");

		return REDIRECT_LIST_EMPLOYEES;
	}

	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee role")
	@GetMapping("/updatePassword/{employeeid}")
	public String updatePassword(@PathVariable String employeeid, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);
		if (optionalEmp.isPresent()) {
			model.addAttribute(EMPLOYEE, optionalEmp.get());
			return "views/fragments/updatePassword";
		} else {
			return REDIRECT_WELCOME;
		}
	}

	@Operation(summary = "Edit an existing employee role", description = "Processes the form submission to edit an existing employee role")
	@PostMapping("/updatePassword/{employeeid}")
	public String updatePassword(@PathVariable String employeeid, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, Model model, RedirectAttributes attributes) {
		Optional<Employees> employeeOpt = employeeService.getEmployeeByEmployeeId(employeeid);
		if (!employeeOpt.isPresent()) {
			attributes.addFlashAttribute("errorMessage", "Employee not found.");
			return REDIRECT_LIST_EMPLOYEES;
		}

		Employees employee = employeeOpt.get();

		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute(EMPLOYEE, employee); // Make sure the attribute name matches your Thymeleaf template
			model.addAttribute("errorMessage", "Passwords do not match.");
			return "views/fragments/updatePassword"; // Ensure this matches the path to your Thymeleaf template
		}

		// Update password
		employeeService.updateEmployeePassword(employeeid, newPassword);

		// Set success message as a flash attribute (for redirect)
		attributes.addFlashAttribute(SUCCESS_MESSAGE,
				"Password updated successfully for employee with ID " + employeeid + ".");

		return REDIRECT_LIST_EMPLOYEES;
	}

	@GetMapping("/employeeprofile/{employeeid}")
	public String userProfile(@PathVariable String employeeid, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);

		if (optionalEmp.isPresent()) {
			model.addAttribute("Profile", optionalEmp.get());
			return "views/pages/profile";
		} else {
			return REDIRECT_WELCOME;
		}
	}
//
//	@Operation(summary = "Delete an employee", description = "Deletes an existing employee")
//	@GetMapping("/delete/{id}")
//	public String deleteEmp(@PathVariable Integer id) {
//		employeeService.deleteEmployee(id);
//		return REDIRECT_LIST_EMPLOYEES;
//	}

	@GetMapping("/employeeForm")
	public String employeeForm() {
		return "views/fragments/addemployeedownloadform";
	}

	@GetMapping("/activeEmployees")
	public String activeList(Model model) {
//		boolean isAdmin = authentication.getAuthorities().stream()
//				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		List<Employees> empList = employeeService.findActiveEmployees();
//		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute(EMPLOYEE, empList);

		return "views/pages/activeEmployeeslist";
	}

	@GetMapping("/updateStatus/{employeeid}")
	public String updateStatus(@PathVariable String employeeid, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeByEmployeeId(employeeid);
		if (optionalEmp.isPresent()) {
			model.addAttribute(EMPLOYEE, optionalEmp.get());
			return "views/fragments/updateStatus";
		} else {
			return REDIRECT_WELCOME;
		}
	}

	@PostMapping("/updateStatus/{employeeid}")
	public String updateStatus(@PathVariable String employeeid, @RequestParam("status") Boolean status,
			RedirectAttributes redirectAttributes) {
		employeeService.updateEmployeeStatus(employeeid, status);
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE,
				"Status updated successfully for employee with ID " + employeeid + ".");
		return REDIRECT_LIST_EMPLOYEES;
	}

}
