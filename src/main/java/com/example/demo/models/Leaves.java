package com.example.demo.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

public class Leaves {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leaveId;
    
   

    @Column(name = "type_of_leave")
    private String typeOfLeave;

    private String reason;

    private String status;

    @Column(name = "date_of_leave")
    private LocalDate dateOfLeave;

    @Column(name = "applied_date")
    private LocalDate appliedDate;
    
    
    @Column(name = "available_Leaves")
    private Integer availableLeaves;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore // Ignore serialization of employee here
    private Employees employee;

	
}