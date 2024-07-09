package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Employees;

public interface EmployeeService {

	List<Employees> getAllEmployees();

	List<Employees> getAllActiveEmployees();

	Optional<Employees> getEmployeeByEmployeeId(String employeeid);

	Employees addEmployee(Employees employee) throws Exception;

	Employees findByFirstname(String firstname);
	
	Employees findByEmployeeId(String employeeid);


	Employees updateEmployee(String employeeid, Employees employee);

	void deleteEmployee(String employeeid);

	void updatePassword(String employeeid, String newPassword);

	void updateEmployeeRole(String employeeid, String role);

	void save(Employees employee);

	List<Employees> getAllEmployeesExceptAdmin();

	void updateEmployeePassword(String employeeid, String password);
	
    boolean isUnique(Employees employee);

	boolean existsByAdhaar(Long adhaar);

	boolean existsByPan(String pan);

	boolean existsByMobile(Long mobile);

	boolean existsByEmail(String email);
	
    List<Employees> findActiveEmployees();
    
	void updateEmployeeStatus(String employeeid, Boolean status);	

	


}