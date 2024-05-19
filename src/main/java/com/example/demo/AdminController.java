package com.example.demo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

	private final EmployeeRepo employeeRepo;

	private final EmployeeServiceImpl employeeService;

	public AdminController(EmployeeServiceImpl employeeService, EmployeeRepo employeeRepo) {
		this.employeeService = employeeService;
		this.employeeRepo = employeeRepo;
	}

	@GetMapping("/login")
	public String adminform() {
		return "/views/pages/login";
	}

//    @GetMapping("/admin/home")
//    public String showEmpList(Model model) {
////    	 int pageSize = 10;
//    	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//           String firstName = authentication.getName();
//           Employee user = employeeRepo.findByFirstname(firstName).orElse(null);
//           if (user == null) {
//               return "error";
//           }
//      model.addAttribute("Profile", user);
//
//    	
//    	
//        List<Employee> empList = employeeService.getAllEmployees();
//        model.addAttribute("Employee", empList);
//        return "/views/pages/homepage";
//    }
    
	@GetMapping("/admin/home")
	public String page(Model model) {
		List<Employee> empList = employeeService.getAllEmployees();
		model.addAttribute("Employee", empList);

		return "/views/pages/homepage";
	}

	@GetMapping("/admin/add")
	public String AddEmpForm(Model model) {
		model.addAttribute("Employee", new Employee());
		return "/views/fragments/addemp";
	}

	@PostMapping("/admin/add")
	public String addEmp(@ModelAttribute("Employee") Employee employee) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String firstName = authentication.getName();
		Employee user = employeeRepo.findByFirstname(firstName).orElse(null);
		employeeService.addEmployee(employee);
		return "redirect:/admin/home";
	}

	@GetMapping("/admin/edit/{id}")
	public String editEmpForm(@PathVariable Integer id, Model model) {
		Optional<Employee> optionalEmp = employeeService.getEmployeeById(id);
		if (optionalEmp.isPresent()) {
			model.addAttribute("Employee", optionalEmp.get());
			return "/views/fragments/editemp";
		} else {
			return "redirect:/admin/home";
		}
	}

	@PostMapping("/admin/edit/{id}")
	public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employee employee) {
		employeeService.updateEmployee(id, employee);
		return "redirect:/admin/home";
	}

	@GetMapping("/admin/delete/{id}")
	public String deleteEmp(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
		return "redirect:/admin/home";
	}
}
