package com.example.demo.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.LeaveDTO;
import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;

public interface LeavesRepository extends JpaRepository<Leaves, Integer> {

	List<Leaves> findByStatus(String status);

	List<Leaves> findByDateOfLeave(LocalDate dateOfLeave);

	Leaves findByLeaveId(Integer leaveId);

	List<Leaves> findByEmployeeAndStatus(Employees employee, String string);

}
