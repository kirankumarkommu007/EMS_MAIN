package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;





@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	Optional<Employee> findByFirstname(String firstname);
	
}
