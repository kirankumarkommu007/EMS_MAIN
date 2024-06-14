package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Employees;

public interface EmployeeService {

	List<Employees> getAllEmployees();

	List<Employees> getAllActiveEmployees();

	Optional<Employees> getEmployeeById(Integer id);

	Employees addEmployee(Employees employee);

	Employees findByFirstname(String firstname);
	
	Employees updateEmployee(Integer id, Employees employee);

	void deleteEmployee(Integer id);

	void updatePassword(String firstname, String newPassword);

	void updateEmployeeRole(Integer id, String role);
	
    void save(Employees employee);
    
    List<Employees> getAllEmployeesExceptAdmin();


	void updateEmployeePassword(Integer id, String password);
}