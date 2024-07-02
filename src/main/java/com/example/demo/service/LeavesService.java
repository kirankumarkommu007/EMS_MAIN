package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LeaveDTO;

public interface LeavesService {

    void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO);
    List<LeaveDTO> getPendingLeaves();
    void denyLeave(Integer leaveId, String denyReason);
    void approveLeave(Integer leaveId);
}
