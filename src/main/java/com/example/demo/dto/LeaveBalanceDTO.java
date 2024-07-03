package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveBalanceDTO {
    private Integer totalLeaves;
    private Integer availableLeaves;
}
