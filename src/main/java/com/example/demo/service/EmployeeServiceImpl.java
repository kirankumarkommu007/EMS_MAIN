package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepo employeeRepo;
	private final PasswordEncoder passwordEncoder;

	public EmployeeServiceImpl(EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
		this.employeeRepo = employeeRepo;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public List<Employees> getAllEmployees() {
		return employeeRepo.findAll();
	}

	@Override
	public List<Employees> getAllActiveEmployees() {
		return employeeRepo.findByStatus(true); // Assuming 'status' field in Employees entity
	}

	@Override
	public Optional<Employees> getEmployeeById(Integer id) {
		return employeeRepo.findById(id);
	}

	@Override
	public Employees addEmployee(Employees employee) {
		String encodedPassword = passwordEncoder.encode(employee.getFirstname());
		employee.setPassword(encodedPassword);
		employee.setRole("USER");
		return employeeRepo.save(employee);
	}

	@Override
	public Employees updateEmployee(Integer id, Employees employee) {
		employee.setId(id);
		return employeeRepo.save(employee);
	}

	@Override
	public void deleteEmployee(Integer id) {
		employeeRepo.deleteById(id);
	}

	@Override
	public void  updatePassword(String firstname, String newPassword) {
		Employees employee = employeeRepo.findByFirstname(firstname);
		
		if (employee != null) {
			String encodedPassword = passwordEncoder.encode(newPassword);
			employee.setPassword(encodedPassword);
			 employeeRepo.save(employee);
		}
	}

	@Override
	public void updateEmployeeRole(Integer id, String role) {
		Optional<Employees> optionalEmp = employeeRepo.findById(id);
		if (optionalEmp.isPresent()) {
			Employees employee = optionalEmp.get();
			employee.setRole(role);
			employeeRepo.save(employee);
		}
	}

	@Override
	public void updateEmployeePassword(Integer id, String newPassword) {
		Optional<Employees> optionalEmp = employeeRepo.findById(id);
		if (optionalEmp.isPresent()) {
			Employees employee = optionalEmp.get();

			String encodedPassword = passwordEncoder.encode(newPassword);
			employee.setPassword(encodedPassword);
			employeeRepo.save(employee);
		}
	}
	@Override
    public void save(Employees employee) {
        employeeRepo.save(employee);
    }

	@Override
	public Employees findByFirstname(String firstname) {
		return employeeRepo.findByFirstname(firstname);
	}
	
	@Override
    public List<Employees> getAllEmployeesExceptAdmin() {
        return employeeRepo.findAllEmployeesExceptAdmin();
    }

}
