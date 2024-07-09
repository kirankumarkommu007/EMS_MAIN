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
    public Optional<Employees> getEmployeeByEmployeeId(String employeeid) {
        return Optional.ofNullable(employeeRepo.findByEmployeeId(employeeid));
    }

    private String generateNextEmployeeId() {
        Long count = employeeRepo.count() + 1;
        return "DVDL-" + String.format("%04d", count);
    }

    @Override
    public Employees addEmployee(Employees employee) throws Exception {
        if (isUnique(employee)) {
            employee.setRole("USER");
            employee.setStatus(true);
            employee.setTotalLeaves(12);
            String nextEmployeeId = generateNextEmployeeId();
            employee.setEmployeeId(nextEmployeeId);
            String encodedPassword = passwordEncoder.encode(employee.getEmployeeId()); // Default password based on employeeId
            employee.setPassword(encodedPassword);
            return employeeRepo.save(employee);
        } else {
            throw new Exception("Employee with given details already exists");
        }
    }

    @Override
    public Employees updateEmployee(String employeeid, Employees employee) {
        Employees existingEmployee = employeeRepo.findByEmployeeId(employeeid);
        if (existingEmployee != null) {
            employee.setEmployeeId(employeeid); // Ensure employeeId remains unchanged
            return employeeRepo.save(employee);
        }
        return null; // Handle appropriately if employee not found
    }

    @Override
    public void deleteEmployee(String employeeid) {
        Employees employee = employeeRepo.findByEmployeeId(employeeid);
        if (employee != null) {
            employeeRepo.delete(employee);
        }
    }

    @Override
    public void updatePassword(String employeeid, String newPassword) {
        Employees employee = employeeRepo.findByEmployeeId(employeeid);
        if (employee != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            employee.setPassword(encodedPassword);
            employeeRepo.save(employee);
        }
    }

    @Override
    public void updateEmployeeRole(String employeeid, String role) {
        Employees employee = employeeRepo.findByEmployeeId(employeeid);
        if (employee != null) {
            employee.setRole(role);
            employeeRepo.save(employee);
        }
    }

    @Override
    public void save(Employees employee) {
        employeeRepo.save(employee);
    }

    @Override
    public List<Employees> getAllEmployeesExceptAdmin() {
        return employeeRepo.findAllEmployeesExceptAdmin();
    }

    @Override
    public void updateEmployeePassword(String employeeid, String password) {
        Employees employee = employeeRepo.findByEmployeeId(employeeid);
        if (employee != null) {
            String encodedPassword = passwordEncoder.encode(password);
            employee.setPassword(encodedPassword);
            employeeRepo.save(employee);
        }
    }

    @Override
    public boolean isUnique(Employees employee) {
        return !existsByAdhaar(employee.getAdhaar()) &&
               !existsByPan(employee.getPan()) &&
               !existsByMobile(employee.getMobile()) &&
               !existsByEmail(employee.getEmail());
    }

    @Override
    public boolean existsByAdhaar(Long adhaar) {
        return employeeRepo.existsByAdhaar(adhaar);
    }

    @Override
    public boolean existsByPan(String pan) {
        return employeeRepo.existsByPan(pan);
    }

    @Override
    public boolean existsByMobile(Long mobile) {
        return employeeRepo.existsByMobile(mobile);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepo.existsByEmail(email);
    }

    @Override
    public List<Employees> findActiveEmployees() {
        return employeeRepo.findByStatusTrue();
    }

    @Override
    public void updateEmployeeStatus(String employeeid, Boolean status) {
        Employees employee = employeeRepo.findByEmployeeId(employeeid);
        if (employee != null) {
            employee.setStatus(status);
            employeeRepo.save(employee);
        }
    }

    @Override
    public Employees findByFirstname(String firstname) {
        return employeeRepo.findByFirstname(firstname);
    }

    @Override
    public Employees findByEmployeeId(String employeeid) {
        return employeeRepo.findByEmployeeId(employeeid);
    }

 

}
