package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveDTO;

public interface LeavesService {

    void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO);
    List<LeaveDTO> getPendingLeaves();
    List<LeaveDTO> employeeOnleave();
    List<LeaveDTO> getMyLeaves(String employeeId);
    
    void denyLeave(Integer leaveId, String denyReason);
    void approveLeave(Integer leaveId);
	void deleteLeave(Integer leaveId);
	LeaveBalanceDTO getLeaveBalance(String employeeId);
}
