package com.example.demo.service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;

public class EmployeeDTOserviceImpl implements EmployeeDTOservice {
	
	private final EmployeeRepo employeeRepo;
	
	public EmployeeDTOserviceImpl(EmployeeRepo employeeRepo) {
		this.employeeRepo=  employeeRepo;
	}
	
	
	   @Override
	    public EmployeeDTO findByEmployeeID(String employeeId) {
	        Employees employee = employeeRepo.findByEmployeeId(employeeId);

	        // Convert Employees entity to EmployeeDTO
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setEmployeeId(employee.getEmployeeId());
	        employeeDTO.setFirstname(employee.getFirstname());
	        employeeDTO.setLastname(employee.getLastname());
	        employeeDTO.setDateOfBirth(employee.getDateOfBirth());

	        return employeeDTO;
	    }

}
