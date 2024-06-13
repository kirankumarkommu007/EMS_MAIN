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
    public Employees updatePassword(String firstname, String email, String newPassword) {
        Employees employee = employeeRepo.findByFirstnameAndEmail(firstname, email);
        if (employee != null) {
            // Encode the new password
            String encodedPassword = passwordEncoder.encode(newPassword);
            // Set the new encoded password
            employee.setPassword(encodedPassword);
            // Save the updated employee
            return employeeRepo.save(employee);
        }
        return null; // Handle error or return Optional.empty()
    }


    
}
