package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO) {
		Optional<Employees> optionalEmployee = employeesRepository.findById(authenticatedEmployeeId);

		if (optionalEmployee.isPresent()) {
			Employees employee = optionalEmployee.get();

			Leaves leave = new Leaves();
			leave.setTypeOfLeave(leaveDTO.getTypeOfLeave());
			leave.setReason(leaveDTO.getReason());
			leave.setStatus("PENDING");
			leave.setDateOfLeave(leaveDTO.getDateOfLeave());
			leave.setAppliedDate(LocalDate.now()); // Set current date for applied date
            leave.setAvailableLeaves(employee.getTotalLeaves());
			leave.setEmployee(employee);
			employee.getLeaves().add(leave);

			employeesRepository.save(employee);
		} else {
			throw new IllegalArgumentException("Authenticated employee not found.");
		}
	}

	@Override
	public List<LeaveDTO> getPendingLeaves() {
		List<Leaves> pendingLeaves = leavesRepository.findByStatus("PENDING");
		return pendingLeaves.stream().map(leave -> new LeaveDTO(leave.getTypeOfLeave(), leave.getReason(),
				leave.getDateOfLeave(), leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves()))
				.collect(Collectors.toList());
	}
	
	@Override
    public void approveLeave(Integer leaveId) {
        Optional<Leaves> optionalLeave = leavesRepository.findById(leaveId);

        if (optionalLeave.isPresent()) {
            Leaves leave = optionalLeave.get();
            leave.setStatus("APPROVED");

            Employees employee = leave.getEmployee();
            employee.setTotalLeaves(employee.getTotalLeaves() - 1);

            leavesRepository.save(leave);
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
}
