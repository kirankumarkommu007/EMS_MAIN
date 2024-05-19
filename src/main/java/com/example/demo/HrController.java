package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class HrController {
	
	 private final EmployeeServiceImpl hrservice;

	 public HrController(EmployeeServiceImpl hrservice) {
	        this.hrservice = hrservice;
	    }
	    
	    
	  @GetMapping("/hr/home")
	    public String showEmpList(Model model) {
	        List<Employee> empList = hrservice.getAllEmployees();
	        model.addAttribute("Employee", empList);
	        return "/views/pages/hrhome";
	    }
	    
	  
	    @GetMapping("/hr/edit/{id}")
	    public String editEmpForm(@PathVariable Integer id, Model model) {
	        Optional<Employee> optionalEmp = hrservice.getEmployeeById(id);
	        if (optionalEmp.isPresent()) {
	            model.addAttribute("Employee", optionalEmp.get());
	            return "/views/fragments/editemp";
	        } else {
	            return "redirect:/hr/home";
	        }
	    }

	    @PostMapping("/hr/edit/{id}")
	    public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employee employee) {
	        hrservice.updateEmployee(id, employee);
	        return "redirect:/hr/home";
	    }


}
