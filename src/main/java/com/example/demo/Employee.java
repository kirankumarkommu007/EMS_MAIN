package com.example.demo;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "NewEmployess")
@Data
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstname;
	private String middlename;
	private String lastname;
	private String department;
	private String gender;
	private String pan;
	private String bloodgroup;

	private Long mobile;
	private Long salary;
	private LocalDate dateOfBirth;
	private String managerid;
	private String designation;
	private String email;
	private String address;
	private LocalDate dateofJoining;
	private LocalDate dateofLeaving; 
	
	//emergency
	private Long emergencycontact;
	private String emergencycontactperson;
	private String relation;
	
	private String confirmpassword;
    private String password;
    private String role;

}