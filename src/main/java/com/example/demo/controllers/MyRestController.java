package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;

@RestController
public class MyRestController {

	@Autowired
	private EmployeeRepo employeeRepo;

	@GetMapping("/greet")
	public String getGreet(@RequestParam String firstname) {
		Employees employee = employeeRepo.findByFirstname(firstname);
		if (employee != null) {
			return "Good Morning " + employee.getFirstname();
		} else {
			return "Employee not found";
		}
	}

	@GetMapping("/admin")
	public String getGreetad() {
		return "hi admin";
	}

	@GetMapping("/getall")
	public List<Employees> getGreetus() {
		return employeeRepo.findAll();
	}

}
