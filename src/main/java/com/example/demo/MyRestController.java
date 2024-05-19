package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("http://localhost:3000/")
public class MyRestController {

	
	@Autowired
	private EmployeeRepo b;
	
	@GetMapping("/admin/allemps")
	public List<Employee> getAll(){
		return b.findAll();
	}
	
}
