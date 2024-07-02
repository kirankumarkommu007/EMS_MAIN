package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.LeaveDTO;
import com.example.demo.models.Leaves;

public interface LeavesRepository extends JpaRepository<Leaves, Integer> {

    List<Leaves> findByStatus(String status);

}
