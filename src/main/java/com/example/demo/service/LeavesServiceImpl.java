package com.example.demo.service;

import com.example.demo.models.Leaves;
import com.example.demo.repos.LeavesRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class LeavesServiceImpl implements LeavesService {

    private final LeavesRepository leavesRepository;

    public LeavesServiceImpl(LeavesRepository leavesRepository) {
        this.leavesRepository = leavesRepository;
    }

    @Override
    @Transactional
    public Leaves saveLeave(Leaves leave) {
        // Set the applied date to the current date
        leave.setStatus("PENDING");
        leave.setAppliedDate(LocalDate.now());
        return leavesRepository.save(leave);
    }
}

