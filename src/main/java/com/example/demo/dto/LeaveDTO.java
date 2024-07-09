package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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
    private Integer days;
    private LocalDate endDate;
    private LocalDate startDate;
    private Integer approvedDays;

    // Constructor with essential fields
    public LeaveDTO(Integer leaveId, String typeOfLeave, String reason, LocalDate dateOfLeave, LocalDate appliedDate, String employeeId) {
        this.leaveId = leaveId;
        this.typeOfLeave = typeOfLeave;
        this.reason = reason;
        this.dateOfLeave = dateOfLeave;
        this.appliedDate = appliedDate;
        this.employeeId = employeeId;
    }

    // Constructor with additional fields
    public LeaveDTO(Integer leaveId, String typeOfLeave, String reason, LocalDate dateOfLeave, LocalDate appliedDate, String employeeId, Integer availableLeaves, String status, LocalDate endDate) {
        this.leaveId = leaveId;
        this.typeOfLeave = typeOfLeave;
        this.reason = reason;
        this.dateOfLeave = dateOfLeave;
        this.appliedDate = appliedDate;
        this.employeeId = employeeId;
        this.availableLeaves = availableLeaves;
        this.status = status;
        this.endDate = endDate;
    }

    // Getters and setters, and any other methods as needed
}

