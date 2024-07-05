package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MyRestController {

    @Autowired
    private EmployeeRepo employeeRepo;
    
  

    private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

    @GetMapping("/greet")
    public String getGreet(@RequestParam String firstname) {
        logger.info("Received request to greet employee with firstname: {}", firstname);
        Employees employee = employeeRepo.findByFirstname(firstname);
        if (employee != null) {
            logger.info("Employee found: {}", employee.getFirstname());
            return "Good Morning " + employee.getFirstname();
        } else {
            logger.warn("Employee not found with firstname: {}", firstname);
            return "Employee not found";
        }
    }
    
    
  
        

      
    


    @GetMapping("/hello")
    public String getGreetad() {
        logger.info("Received request to greet the application");
        return "hi this is EMS application";
    }

    @GetMapping("/getall")
    public List<Employees> getGreetus() {
        logger.info("Received request to get all employees");
        return employeeRepo.findAll();
    }

}
