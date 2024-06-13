package com.example.demo.models;

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
@Data
@Getter
@Setter
public class Employees {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Personal details
	private String firstname;
	private String middlename;
	private String lastname;
	private LocalDate dateOfBirth;
	private String bloodgroup;
	private String gender;
	private String pan;
	private String adhaar;

	private Long mobile;
	private String email;
	private String fathername;
	private String mothername;
	private String spousename;
	private String maritalStatus;
	private String permanentaddress;
	private String communicationaddress;

	// Educational details
	private String highestqualification;
	private String qualifyingbranch;
	private Long yearOfPassing;
	private String university;
	private String collegeaddress;
	private String technicalSkills;
	private String technicalCertification;
	private Double cgpaPercentage;

	// Present employment details
	private Long salary;
	private String managerId;
	private String designation;
	private String department;
	private LocalDate dateOfJoining;
	private LocalDate dateOfLeaving;

	// Previous employment details
	private Integer yearsexperience;
	private String jobRole;
	private String previousCompany;
	private String uanNumber;
	private LocalDate dateOfLeavingCompany;

	// Emergency contact
	private String emergencyContactPerson1;
	private Long emergencyContactPerson1mobile;
	private String emergencyContactPerson1email;
	private String emergencyContactPerson1relation;

	private String emergencyContactPerson2;
	private Long emergencyContactPerson2mobile;
	private String emergencyContactPerson2email;
	private String emergencyContactPerson2relation;

	// Login details
	private String role;
	private String password;
}
