package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Employees;
import com.example.demo.service.EmployeeServiceImpl;




@Controller
@Tag(name = "Admin Controller", description = "Controller for managing employee data by admin")
public class AdminController {

	
	private final EmployeeServiceImpl  employeeServiceImpl;

	public AdminController(EmployeeServiceImpl  employeeServiceImpl) {
		this.employeeServiceImpl=employeeServiceImpl;
	}

    @Operation(summary = "View admin home page", description = "Displays the admin home page with a list of employees")
    @GetMapping("/admin/home")
    public String page(Model model) {     
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Employees user = employeeServiceImpl.findByEmployeeId(authentication.getName());
		model.addAttribute("user",user);
        return "views/pages/homepage";
    }
    
    
    @GetMapping("/mycorner")
    public String myview(Model model) {     
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Employees user = employeeServiceImpl.findByEmployeeId(authentication.getName());
		model.addAttribute("user",user);
        return "views/pages/mycorner";
    }
    
    
   
}
