package com.example.demo.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import correct Model interface
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LeaveDTO;
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
    public String showLeaveRequestForm(Model model) {
        model.addAttribute("leave", new LeaveDTO()); // Prepare a new LeaveDTO object for the form
        return "views/fragments/LeaveRequestForm"; // Assuming a view named leaveRequestForm exists
    }

    @PostMapping("/submitLeaveRequest")
    public String submitLeave(@ModelAttribute("leave") LeaveDTO leaveDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmployeeId = auth.getName(); // Assuming authenticated user's username is the employeeId
        leaveService.addLeaves(authenticatedEmployeeId, leaveDTO);
        return "redirect:/leaveRequest";
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public String viewPendingLeaves(Model model) {
        List<LeaveDTO> pendingLeaves = leaveService.getPendingLeaves();
        model.addAttribute("pendingLeaves", pendingLeaves);
        return "views/fragments/pendingLeaves";
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public String approveLeave(@RequestParam("leaveId") Integer leaveId) {
        leaveService.approveLeave(leaveId);
        return "redirect:/leaves/pending";
    }

    @PostMapping("/deny")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public String denyLeave(@RequestParam("leaveId") Integer leaveId, @RequestParam("denyReason") String denyReason) {
        leaveService.denyLeave(leaveId, denyReason);
        return "redirect:/leaves/pending";
    }
}
