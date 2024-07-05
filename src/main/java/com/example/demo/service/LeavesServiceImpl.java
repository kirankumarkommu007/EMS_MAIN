package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveDTO;
import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.repos.LeavesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeavesServiceImpl implements LeavesService {

	@Autowired
	private EmployeeRepo employeesRepository;

	@Autowired
	private LeavesRepository leavesRepository;


//	@Override
//	public void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO) {
//	    Optional<Employees> optionalEmployee = employeesRepository.findById(authenticatedEmployeeId);
//
//	    if (optionalEmployee.isPresent()) {
//	        Employees employee = optionalEmployee.get();
//
//	        // Check if there are pending leaves
//	        boolean hasPendingLeaves = employee.getLeaves().stream()
//	                .anyMatch(leave -> leave.getStatus().equals("PENDING"));
//
//	        if (hasPendingLeaves) {
//	            throw new IllegalStateException("You already have pending leave(s). Please look into them first.");
//	        }
//
//	        // Proceed to add new leave
//	        Leaves leave = new Leaves();
//	        leave.setTypeOfLeave(leaveDTO.getTypeOfLeave());
//	        leave.setReason(leaveDTO.getReason());
//	        leave.setStatus("PENDING");
//	        leave.setAppliedDate(LocalDate.now()); // Set current date for applied date
//	        leave.setDays(leaveDTO.getDays()); // Set days from leaveDTO
//
//	        if (leaveDTO.getDays() == 1) {
//	            leave.setDateOfLeave(leaveDTO.getDateOfLeave());
//	            leave.setEndDate(leaveDTO.getDateOfLeave()); // endDate is same as dateOfLeave for single day
//	            System.out.println(leaveDTO.getDateOfLeave());
//	        } else {
//	            leave.setDateOfLeave(leaveDTO.getStartDate());
//	            System.out.println("showing start date for multiple dates"+leaveDTO.getDateOfLeave());
//
//	            leave.setEndDate(leaveDTO.getEndDate());
//	            System.out.println("showing end date for multiple dates"+leaveDTO.getEndDate());
//
//	        }
//
//	        // Set availableLeaves based on calculation
//	        long approvedLeavesCount = employee.getLeaves().stream()
//	                .filter(l -> l.getStatus().equals("APPROVED"))
//	                .count();
//	        int availableLeaves = employee.getTotalLeaves() - (int) approvedLeavesCount;
//	        if (availableLeaves <= 0) {
//	            throw new IllegalArgumentException("You have 0 available leaves. Please contact administration.");
//	        } else {
//	            leave.setAvailableLeaves(availableLeaves);
//
//	            leave.setEmployee(employee);
//	            employee.getLeaves().add(leave);
//
//	            employeesRepository.save(employee);
//	        }
//
//	    } else {
//	        throw new IllegalArgumentException("Authenticated employee not found.");
//	    }
//	}
	
	@Override
	public void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO) {
	    Optional<Employees> optionalEmployee = employeesRepository.findById(authenticatedEmployeeId);

	    if (optionalEmployee.isPresent()) {
	        Employees employee = optionalEmployee.get();

//	        // Check if there are pending leaves
//	        boolean hasPendingLeaves = employee.getLeaves().stream()
//	                .anyMatch(leave -> leave.getStatus().equals("PENDING"));
//
//	        if (hasPendingLeaves) {
//	            throw new IllegalStateException("You already have pending leave(s). Please look into them first.");
//	        }

	        // Proceed to add new leave
	        Leaves leave = new Leaves();
	        leave.setTypeOfLeave(leaveDTO.getTypeOfLeave());
	        leave.setReason(leaveDTO.getReason());
	        leave.setStatus("PENDING");
	        leave.setAppliedDate(LocalDate.now()); // Set current date for applied date
	        leave.setDays(leaveDTO.getDays()); // Set days from leaveDTO

	        if (leaveDTO.getDays() == 1) {
	            leave.setDateOfLeave(leaveDTO.getDateOfLeave());
	            leave.setEndDate(leaveDTO.getDateOfLeave()); // endDate is same as dateOfLeave for single day
	        } else {
	            leave.setDateOfLeave(leaveDTO.getStartDate());
	            leave.setEndDate(leaveDTO.getEndDate());
	        }

	        // Calculate approved days for the employee
	        int approvedDays = employee.getLeaves().stream()
	                .filter(l -> l.getStatus().equals("APPROVED"))
	                .mapToInt(Leaves::getDays)
	                .sum();

	        // Set availableLeaves based on calculation
	        int availableLeaves = employee.getTotalLeaves() - approvedDays;
	        if (availableLeaves <= 0) {
	            throw new IllegalArgumentException("You have 0 available leaves. Please contact administration.");
	        } else {
	            leave.setAvailableLeaves(availableLeaves);
//	            leave.setApprovedDays(approvedDays); // Set approved days

	            leave.setEmployee(employee);
	            employee.getLeaves().add(leave);

	            employeesRepository.save(employee);
	        }

	    } else {
	        throw new IllegalArgumentException("Authenticated employee not found.");
	    }
	}



	    

	@Override
	public List<LeaveDTO> getPendingLeaves() {
		List<Leaves> pendingLeaves = leavesRepository.findByStatus("PENDING");
		return pendingLeaves.stream().map(leave -> new LeaveDTO(leave.getLeaveId(),leave.getTypeOfLeave(), leave.getReason(),
				leave.getDateOfLeave(), leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
				.collect(Collectors.toList());
	}
	@Override
	public void approveLeave(Integer leaveId) {
	    Optional<Leaves> optionalLeave = leavesRepository.findById(leaveId);

	    if (optionalLeave.isPresent()) {
	        Leaves leave = optionalLeave.get();
	        Employees employee = leave.getEmployee();

	        // Number of days of the approved leave
	        int approvedDays = leave.getDays();

	        // Approve the leave
	        leave.setStatus("APPROVED");
	        leavesRepository.save(leave);

	        // Find all pending leaves for the same employee
	        List<Leaves> pendingLeaves = leavesRepository.findByEmployeeAndStatus(employee, "PENDING");

	        // Decrease availableLeaves by the number of approved days for each pending leave
	        for (Leaves pendingLeave : pendingLeaves) {
	            pendingLeave.setAvailableLeaves(pendingLeave.getAvailableLeaves() - approvedDays);
	            leavesRepository.save(pendingLeave);
	        }

	        // Save the employee
	        employeesRepository.save(employee);
	    } else {
	        throw new IllegalArgumentException("Leave not found.");
	    }
	}


	@Override
    public void denyLeave(Integer leaveId, String denyReason) {
        Optional<Leaves> optionalLeave = leavesRepository.findById(leaveId);

        if (optionalLeave.isPresent()) {
            Leaves leave = optionalLeave.get();
            leave.setStatus("DENIED");
            leave.setReason(denyReason);

            leavesRepository.save(leave);
        } else {
            throw new IllegalArgumentException("Leave not found.");
        }
    }
    
    
    

	@Override
    public List<LeaveDTO> getMyLeaves(String employeeId) {
        Employees employee = employeesRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found."));
        return employee.getLeaves().stream()
                .map(leave -> new LeaveDTO(leave.getLeaveId(), leave.getTypeOfLeave(), leave.getReason(), leave.getDateOfLeave(), 
                		leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
                .collect(Collectors.toList());
    }

    
	@Override
	public LeaveBalanceDTO getLeaveBalance(String employeeId) {
	    Employees employee = employeesRepository.findById(employeeId)
	        .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
	    
	    int totalLeaves = employee.getTotalLeaves();
	    int usedLeaveDays = employee.getLeaves().stream()
	        .filter(leave -> "APPROVED".equals(leave.getStatus()))
	        .mapToInt(leave -> leave.getDays())
	        .sum();
	    
	    int availableLeaves = totalLeaves - usedLeaveDays;

	    return new LeaveBalanceDTO(totalLeaves, availableLeaves);
	}


	@Override
	public List<LeaveDTO> employeeOnleave() {
		List<Leaves> pendingLeaves = leavesRepository.findByDateOfLeave(LocalDate.now());
		return pendingLeaves.stream().map(leave -> new LeaveDTO(leave.getLeaveId(),leave.getTypeOfLeave(), leave.getReason(),
				leave.getDateOfLeave(), leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
				.collect(Collectors.toList());
	}


	@Override
	public void deleteLeave(Integer leaveId) {
		Leaves leave = leavesRepository.findByLeaveId(leaveId);
	        if (leave != null) {
	            leavesRepository.delete(leave);
	        }
	    }
	
}
