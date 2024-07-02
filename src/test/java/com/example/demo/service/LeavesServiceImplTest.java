package com.example.demo.service;

import com.example.demo.dto.LeaveDTO;
import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.repos.LeavesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LeavesServiceImplTest {

    @Mock
    private EmployeeRepo employeesRepository;

    @Mock
    private LeavesRepository leavesRepository;

    @InjectMocks
    private LeavesServiceImpl leavesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

  

    @Test
    public void testAddLeaves_EmployeeNotFound() {
        // Mock authenticated employee
        String authenticatedEmployeeId = "emp123";

        // Mock repository behavior for employee not found
        when(employeesRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call service method and assert exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leavesService.addLeaves(authenticatedEmployeeId, new LeaveDTO());
        });

        // Verify interactions
        verify(employeesRepository, times(1)).findById(authenticatedEmployeeId);
        verify(leavesRepository, never()).save(any(Leaves.class));

        // Assert the exception message
        assert exception.getMessage().contains("Authenticated employee not found.");
    }
}
