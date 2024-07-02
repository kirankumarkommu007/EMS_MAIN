package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.example.demo.models.Employees;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDTO {

    private Integer leaveId;
    private String typeOfLeave;
    private String reason;
    private String status;
    private LocalDate dateOfLeave;
    private LocalDate appliedDate;
    private Integer availableLeaves;
    private String employeeId;

    // Add other relevant fields as needed
    public LeaveDTO(String typeOfLeave, String reason, LocalDate dateOfLeave, LocalDate appliedDate, String employeeId,Integer availableLeaves) {
        this.typeOfLeave = typeOfLeave;
        this.reason = reason;
        this.dateOfLeave = dateOfLeave;
        this.appliedDate = appliedDate;
        this.employeeId = employeeId;
        this.availableLeaves=availableLeaves;
    }
}
