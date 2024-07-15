package com.example.demo.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.demo.controllers.AdminController;
import com.example.demo.models.Employees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }

    @Test
     void testPage() {
        Authentication authentication = mock(Authentication.class);
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testEmployeeId");
        Employees mockEmployee = new Employees();
        mockEmployee.setEmployeeId("testEmployeeId");
        when(employeeService.findByEmployeeId(anyString())).thenReturn(mockEmployee);
        String viewName = adminController.page(model);
        verify(employeeService, times(1)).findByEmployeeId("testEmployeeId");
        verify(model, times(1)).addAttribute("user", mockEmployee);
        assertEquals("views/pages/homepage", viewName);
    }

}
