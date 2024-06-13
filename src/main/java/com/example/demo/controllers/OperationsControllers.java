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

//	@Operation(summary = "Edit an existing employee", description = "Processes the form submission to edit an existing employee")
//	@PostMapping("/edit/{id}")
//	public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employees employee) {
//		employeeService.updateEmployee(id, employee);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//	      if (authentication != null && authentication.getAuthorities().stream()
//	                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//	            return "redirect:/admin/home";
//	        } else if (authentication != null && authentication.getAuthorities().stream()
//	                .anyMatch(a -> a.getAuthority().equals("ROLE_HR"))) {
//	            return "redirect:/hr/home";
//	        }
//	      
//
//	        // Default redirect if no specific role matches
//	        return "redirect:/home";
//	    }	
	
	
	
	@PostMapping("/edit/{id}")
    public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employees updatedEmployee) {
        Optional<Employees> optionalEmp = employeeService.getEmployeeById(id);

        if (optionalEmp.isPresent()) {
            Employees existingEmployee = optionalEmp.get();

            // Only update fields if they are not null or empty
            if (updatedEmployee.getFirstname() != null && !updatedEmployee.getFirstname().isEmpty()) {
                existingEmployee.setFirstname(updatedEmployee.getFirstname());
            }
            if (updatedEmployee.getMiddlename() != null && !updatedEmployee.getMiddlename().isEmpty()) {
                existingEmployee.setMiddlename(updatedEmployee.getMiddlename());
            }
            if (updatedEmployee.getLastname() != null && !updatedEmployee.getLastname().isEmpty()) {
                existingEmployee.setLastname(updatedEmployee.getLastname());
            }
            if (updatedEmployee.getDateOfBirth() != null) {
                existingEmployee.setDateOfBirth(updatedEmployee.getDateOfBirth());
            }
            if (updatedEmployee.getGender() != null && !updatedEmployee.getGender().isEmpty()) {
                existingEmployee.setGender(updatedEmployee.getGender());
            }
            if (updatedEmployee.getBloodgroup() != null && !updatedEmployee.getBloodgroup().isEmpty()) {
                existingEmployee.setBloodgroup(updatedEmployee.getBloodgroup());
            }
            if (updatedEmployee.getMobile() != null && !updatedEmployee.getMobile().isEmpty()) {
                existingEmployee.setMobile(updatedEmployee.getMobile());
            }
            if (updatedEmployee.getEmail() != null && !updatedEmployee.getEmail().isEmpty()) {
                existingEmployee.setEmail(updatedEmployee.getEmail());
            }
            if (updatedEmployee.getPan() != null && !updatedEmployee.getPan().isEmpty()) {
                existingEmployee.setPan(updatedEmployee.getPan());
            }
            if (updatedEmployee.getAdhaar() != null && !updatedEmployee.getAdhaar().isEmpty()) {
                existingEmployee.setAdhaar(updatedEmployee.getAdhaar());
            }
            if (updatedEmployee.getFathername() != null && !updatedEmployee.getFathername().isEmpty()) {
                existingEmployee.setFathername(updatedEmployee.getFathername());
            }
            if (updatedEmployee.getMothername() != null && !updatedEmployee.getMothername().isEmpty()) {
                existingEmployee.setMothername(updatedEmployee.getMothername());
            }
            if (updatedEmployee.getMaritalStatus() != null && !updatedEmployee.getMaritalStatus().isEmpty()) {
                existingEmployee.setMaritalStatus(updatedEmployee.getMaritalStatus());
            }
            if (updatedEmployee.getSpousename() != null && !updatedEmployee.getSpousename().isEmpty()) {
                existingEmployee.setSpousename(updatedEmployee.getSpousename());
            }
            if (updatedEmployee.getPermanentaddress() != null && !updatedEmployee.getPermanentaddress().isEmpty()) {
                existingEmployee.setPermanentaddress(updatedEmployee.getPermanentaddress());
            }
            if (updatedEmployee.getCommunicationaddress() != null && !updatedEmployee.getCommunicationaddress().isEmpty()) {
                existingEmployee.setCommunicationaddress(updatedEmployee.getCommunicationaddress());
            }
            if (updatedEmployee.getHighestqualification() != null && !updatedEmployee.getHighestqualification().isEmpty()) {
                existingEmployee.setHighestqualification(updatedEmployee.getHighestqualification());
            }
            if (updatedEmployee.getQualifyingbranch() != null && !updatedEmployee.getQualifyingbranch().isEmpty()) {
                existingEmployee.setQualifyingbranch(updatedEmployee.getQualifyingbranch());
            }
            if (updatedEmployee.getYearOfPassing() != null && !updatedEmployee.getYearOfPassing().isEmpty()) {
                existingEmployee.setYearOfPassing(updatedEmployee.getYearOfPassing());
            }
            if (updatedEmployee.getUniversity() != null && !updatedEmployee.getUniversity().isEmpty()) {
                existingEmployee.setUniversity(updatedEmployee.getUniversity());
            }
            if (updatedEmployee.getCollegeaddress() != null && !updatedEmployee.getCollegeaddress().isEmpty()) {
                existingEmployee.setCollegeaddress(updatedEmployee.getCollegeaddress());
            }
            if (updatedEmployee.getCgpaPercentage() != null && !updatedEmployee.getCgpaPercentage().isEmpty()) {
                existingEmployee.setCgpaPercentage(updatedEmployee.getCgpaPercentage());
            }
            if (updatedEmployee.getTechnicalCertification() != null && !updatedEmployee.getTechnicalCertification().isEmpty()) {
                existingEmployee.setTechnicalCertification(updatedEmployee.getTechnicalCertification());
            }
            if (updatedEmployee.getTechnicalSkills() != null && !updatedEmployee.getTechnicalSkills().isEmpty()) {
                existingEmployee.setTechnicalSkills(updatedEmployee.getTechnicalSkills());
            }
            if (updatedEmployee.getDepartment() != null && !updatedEmployee.getDepartment().isEmpty()) {
                existingEmployee.setDepartment(updatedEmployee.getDepartment());
            }
            if (updatedEmployee.getManagerId() != null && !updatedEmployee.getManagerId().isEmpty()) {
                existingEmployee.setManagerId(updatedEmployee.getManagerId());
            }
            if (updatedEmployee.getSalary() != null && !updatedEmployee.getSalary().isEmpty()) {
                existingEmployee.setSalary(updatedEmployee.getSalary());
            }
            if (updatedEmployee.getDesignation() != null && !updatedEmployee.getDesignation().isEmpty()) {
                existingEmployee.setDesignation(updatedEmployee.getDesignation());
            }
            if (updatedEmployee.getDateOfJoining() != null) {
                existingEmployee.setDateOfJoining(updatedEmployee.getDateOfJoining());
            }
            if (updatedEmployee.getDateOfLeaving() != null) {
                existingEmployee.setDateOfLeaving(updatedEmployee.getDateOfLeaving());
            }
            if (updatedEmployee.getYearsexperience() != null && !updatedEmployee.getYearsexperience().isEmpty()) {
                existingEmployee.setYearsexperience(updatedEmployee.getYearsexperience());
            }
            if (updatedEmployee.getJobRole() != null && !updatedEmployee.getJobRole().isEmpty()) {
            	existingEmployee.setJobRole(updatedEmployee.getJobRole());
            }
            if (updatedEmployee.getPreviousCompany() != null && !updatedEmployee.getPreviousCompany().isEmpty()) {
                existingEmployee.setPreviousCompany(updatedEmployee.getPreviousCompany());
            }
            if (updatedEmployee.getUanNumber() != null && !updatedEmployee.getUanNumber().isEmpty()) {
                existingEmployee.setUanNumber(updatedEmployee.getUanNumber());
            }
            if (updatedEmployee.getDateOfLeavingCompany() != null) {
                existingEmployee.setDateOfLeavingCompany(updatedEmployee.getDateOfLeavingCompany());
            }
            if (updatedEmployee.getEmergencyContactPerson1() != null && !updatedEmployee.getEmergencyContactPerson1().isEmpty()) {
                existingEmployee.setEmergencyContactPerson1(updatedEmployee.getEmergencyContactPerson1());
            }
            if (updatedEmployee.getEmergencyContactPerson1mobile() != null && !updatedEmployee.getEmergencyContactPerson1mobile().isEmpty()) {
                existingEmployee.setEmergencyContactPerson1mobile(updatedEmployee.getEmergencyContactPerson1mobile());
            }
            if (updatedEmployee.getEmergencyContactPerson1email() != null && !updatedEmployee.getEmergencyContactPerson1email().isEmpty()) {
                existingEmployee.setEmergencyContactPerson1email(updatedEmployee.getEmergencyContactPerson1email());
            }
            if (updatedEmployee.getEmergencyContactPerson1relation() != null && !updatedEmployee.getEmergencyContactPerson1relation().isEmpty()) {
                existingEmployee.setEmergencyContactPerson1relation(updatedEmployee.getEmergencyContactPerson1relation());
            }
            if (updatedEmployee.getEmergencyContactPerson2() != null && !updatedEmployee.getEmergencyContactPerson2().isEmpty()) {
                existingEmployee.setEmergencyContactPerson2(updatedEmployee.getEmergencyContactPerson2());
            }
            if (updatedEmployee.getEmergencyContactPerson2mobile() != null && !updatedEmployee.getEmergencyContactPerson2mobile().isEmpty()) {
                existingEmployee.setEmergencyContactPerson2mobile(updatedEmployee.getEmergencyContactPerson2mobile());
            }
            if (updatedEmployee.getEmergencyContactPerson2email() != null && !updatedEmployee.getEmergencyContactPerson2email().isEmpty()) {
                existingEmployee.setEmergencyContactPerson2email(updatedEmployee.getEmergencyContactPerson2email());
            }
            if (updatedEmployee.getEmergencyContactPerson2relation() != null && !updatedEmployee.getEmergencyContactPerson2relation().isEmpty()) {
                existingEmployee.setEmergencyContactPerson2relation(updatedEmployee.getEmergencyContactPerson2relation());
            }

            employeeService.save(existingEmployee);
            return "redirect:/admin/home";
        } else {
            // Handle the case where the employee is not found
            return "redirect:/admin/home?error=EmployeeNotFound";
        }
    
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee role")
	@GetMapping("/updateRole/{id}")
	public String updateRole(@PathVariable Integer id, Model model) {
	    Optional<Employees> optionalEmp = employeeService.getEmployeeById(id);
	    if (optionalEmp.isPresent()) {
	        model.addAttribute("Employee", optionalEmp.get());
	        return "/views/fragments/updateRole";
	    } else {
	        return "redirect:/admin/home";
	    }
	}

	@Operation(summary = "Edit an existing employee role", description = "Processes the form submission to edit an existing employee role")
	@PostMapping("/updateRole/{id}")
	public String updateRole(@PathVariable Integer id, @RequestParam("role") String role) {
	    employeeService.updateEmployeeRole(id, role);
	    return "redirect:/admin/home";
	}
	
	
	@Operation(summary = "Show edit employee form", description = "Displays the form for editing an existing employee role")
	@GetMapping("/updatePassword/{id}")
	public String updatePassword(@PathVariable Integer id, Model model) {
	    Optional<Employees> optionalEmp = employeeService.getEmployeeById(id);
	    if (optionalEmp.isPresent()) {
	        model.addAttribute("Employee", optionalEmp.get());
	        return "/views/fragments/updatePassword";
	    } else {
	        return "redirect:/admin/home";
	    }
	}

	@Operation(summary = "Edit an existing employee role", description = "Processes the form submission to edit an existing employee role")
	@PostMapping("/updatePassword/{id}")
	public String updatePassword(@PathVariable Integer id, @RequestParam("password") String newpassword) {
	    employeeService.updateEmployeePassword(id, newpassword);
	    return "redirect:/admin/home";
	}

	
	
	
	
	
	
	

	@GetMapping("/employeessprofile/{id}")
	public String userProfile(@PathVariable Integer id, Model model) {
		Optional<Employees> optionalEmp = employeeService.getEmployeeById(id);

		if (optionalEmp.isPresent()) {
			model.addAttribute("Profile", optionalEmp.get());
			return "/profile";
		} else {
			return "redirect:/admin/home";
		}
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
