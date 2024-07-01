package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Leaves;

public interface LeavesRepository extends JpaRepository<Leaves, Integer> {

}
