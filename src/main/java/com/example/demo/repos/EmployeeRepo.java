package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Employees;

@Repository
public interface EmployeeRepo extends JpaRepository<Employees, Integer> {

	Employees findByFirstnameAndId(String firstname, Integer id);

	Employees findByFirstname(String firstname);

	Employees findByLastname(String lastname);


}
