package com.example.demo.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import correct Model interface
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.service.EmployeeServiceImpl;
import com.example.demo.service.LeavesServiceImpl;

@Controller
public class LeaveManagementControllers {
	
	@Autowired
	private  EmployeeServiceImpl employeeServiceImpl ;
	
	@Autowired
	private AuthenticationManager authenticationManager;


    @Autowired
    private LeavesServiceImpl leaveService; // Assuming you have a service layer

    @GetMapping("/leaveRequest")
    public String showLeaveRequestForm(Model model, Leaves leaves) {
        model.addAttribute("leave", leaves); // Correct usage of addAttribute
        return "views/fragments/leaveRequestForm";
    }

    @PostMapping("/submitLeaveRequest")
    public String submitLeave(@ModelAttribute("leave") Leaves leave) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Employees employee = employeeServiceImpl.findByEmployeeId(name);
        leave.setEmployee(employee); // Set the employee for the leave
        leaveService.saveLeave(leave); // Save the leave
        return "redirect:/leaveRequest"; 
    }
}
