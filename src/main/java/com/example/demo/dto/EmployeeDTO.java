package com.example.demo.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {

    private String employeeId;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    // Add other relevant fields as needed
}
