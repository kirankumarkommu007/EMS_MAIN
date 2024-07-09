package com.example.demo.service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension for JUnit 5
public class EmployeeDTOserviceImplTest {

    @InjectMocks
    private EmployeeDTOserviceImpl employeeDTOservice;

    @Mock
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setUp() {
        // Setup mock behavior if needed
    }

    @Test
    void testFindByEmployeeID() {
        // Mock data
        Employees mockEmployee = new Employees();
        mockEmployee.setEmployeeId("EMP001");
        mockEmployee.setFirstname("John");
        mockEmployee.setLastname("Doe");
        mockEmployee.setDateOfBirth(LocalDate.of(1990, 5, 15));

        String employeeId ="EMP001";
		// Mock repository method
        when(employeeRepo.findByEmployeeId(employeeId )).thenReturn(mockEmployee);

        // Call the service method
        EmployeeDTO employeeDTO = employeeDTOservice.findByEmployeeID(employeeId);

        // Assertions
        assertEquals(employeeId, employeeDTO.getEmployeeId());
        assertEquals("John", employeeDTO.getFirstname());
        assertEquals("Doe", employeeDTO.getLastname());
        assertEquals(LocalDate.of(1990, 5, 15), employeeDTO.getDateOfBirth());
    }
}
