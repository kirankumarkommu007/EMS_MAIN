package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Employees;



public interface EmployeeService {

    List<Employees> getAllEmployees();
    Optional<Employees> getEmployeeById(Integer id);
    Employees addEmployee(Employees employee);
    Employees updateEmployee(Integer id, Employees employee);
    void deleteEmployee(Integer id);
}