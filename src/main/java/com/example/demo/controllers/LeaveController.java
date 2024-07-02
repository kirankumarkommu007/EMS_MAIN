package com.example.demo.controllers;

import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.repos.LeavesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeavesRepository leavesRepository;
    
    @Autowired
    private EmployeeRepo employeeRepo;

    @PostMapping
    public ResponseEntity<Leaves> addLeave(@AuthenticationPrincipal Employees employee, @RequestBody Leaves leave) {
        leave.setEmployee(employee);
        leave.setAppliedDate(LocalDate.now());
        Leaves savedLeave = leavesRepository.save(leave);

        // Update the total leaves for the employee
        employee.setTotalLeaves(employee.getTotalLeaves() - 1);
        employeeRepo.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedLeave);
    }
}