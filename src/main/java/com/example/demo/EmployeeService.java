package com.example.demo;

import java.util.List;
import java.util.Optional;



public interface EmployeeService {

    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Integer id);
    Employee addEmployee(Employee employee);
    Employee updateEmployee(Integer id, Employee employee);
    void deleteEmployee(Integer id);
}