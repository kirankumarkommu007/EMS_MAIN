package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.service.EmployeeServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "HR Controller", description = "Controller for managing HR-related operations")
public class HrController {
	
    
    
    private final EmployeeRepo employeeRepo;
    private final EmployeeServiceImpl hrservice;

    public HrController(EmployeeServiceImpl hrservice, EmployeeRepo employeeRepo) {
        this.hrservice = hrservice;
        this.employeeRepo = employeeRepo;
    }
    @Operation(summary = "Show add employee form", description = "Displays the form for adding a new employee")
    @GetMapping("/hr/add")
    public String AddEmpForm(Model model) {
        model.addAttribute("Employee", new Employees());
        return "/views/fragments/hraddemp";
    }    
    
    
    
    @Operation(summary = "Show HR home page", description = "Displays the HR home page with a list of employees")
    @GetMapping("/hr/home")
    public String showEmpList(Model model) {
        List<Employees> empList = hrservice.getAllEmployees();
        model.addAttribute("Employee", empList);
        return "/views/pages/hrhome";
    }
    
    @Operation(summary = "Show edit employee form", description = "Displays the form for editing an employee")
    @GetMapping("/hr/edit/{id}")
    public String editEmpForm(@PathVariable Integer id, Model model) {
        Optional<Employees> optionalEmp = hrservice.getEmployeeById(id);
        if (optionalEmp.isPresent()) {
            model.addAttribute("Employee", optionalEmp.get());
            return "/views/fragments/hreditemp";
        } else {
            return "redirect:/hr/home";
        }
    }

    @Operation(summary = "Edit an employee", description = "Processes the form submission to edit an employee")
    @PostMapping("/hr/edit/{id}")
    public String editEmp(@PathVariable Integer id, @ModelAttribute("Employee") Employees employee) {
        hrservice.updateEmployee(id, employee);
        return "redirect:/hr/home";
    }
}
