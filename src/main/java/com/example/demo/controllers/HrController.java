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
    
    
    
    @Operation(summary = "Show HR home page", description = "Displays the HR home page with a list of employees")
    @GetMapping("/hr/home")
    public String showEmpList(Model model) {
        return "/views/pages/hrhome";
    }
    
 
}
