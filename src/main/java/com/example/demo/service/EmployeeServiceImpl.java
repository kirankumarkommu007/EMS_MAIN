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
	
	
	
	
	
	 private String generateNextEmployeeId() {
		    
	        Long count = (employeeRepo.count() + 1);
	        return "DVDL-" + String.format("%04d", count);
	    }
	 
	 @Override
	    public Employees addEmployee(Employees employee) throws Exception {
	        if (isUnique(employee)) {
	            employee.setRole("USER");
	            employee.setStatus(true);
	            String nextEmployeeId = generateNextEmployeeId();
	            employee.setEmployeeId(nextEmployeeId);
	            String encodedPassword = passwordEncoder.encode(employee.getEmployeeId());
	            employee.setPassword(encodedPassword);
	            return employeeRepo.save(employee);
	        } else {
	            throw new Exception("Employee with given details already exists");
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

	public List<Employees> getEmployeesByRole(String role) {
		return employeeRepo.findByRole(role);
	}

	@Override
	public List<Employees> findActiveEmployees() {
		return employeeRepo.findByStatusTrue();
	}

	@Override
	public void updateEmployeeStatus(Integer id, Boolean status) {
		Optional<Employees> optionalEmp = employeeRepo.findById(id);
		if (optionalEmp.isPresent()) {
			Employees employee = optionalEmp.get();
			employee.setStatus(status);
			employeeRepo.save(employee);
		}		
	}

	@Override
	public Employees findByEmployeeId(String employeeid) {
		return employeeRepo.findByEmployeeId(employeeid);
	}
	

	
}
