package com.example.demo.models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
public class Employees {

	
	
	@Id
    @Column(name = "employee_id", nullable = false, unique = true)
	private String employeeId;
	// Personal details
	@NotBlank(message = "First name is required")
	private String firstname;

	private String middlename;

	@NotBlank(message = "Last name is required")
	private String lastname;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	private String bloodgroup;

	private String gender;

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "[A-Z]{5}\\d{4}[A-Z]", message = "PAN must be in the format: ABCDE1234F")
	private String pan;

	@Column(unique = true, nullable = false)
	@Digits(integer = 12, fraction = 0, message = "Aadhar number must be exactly 12 digits")
	private Long adhaar;

	@Column(unique = true, nullable = false)
	@Digits(integer = 10, fraction = 0, message = "Mobile number must be exactly 10 digits")
	private Long mobile;

	@Column(unique = true, nullable = false)
	@Email(message = "Email should be valid")
	private String email;

	private String fathername;

	private String mothername;

	private String spousename;

	@Column(name = "marital_status")
	private String maritalStatus;

	@Column(name = "permanent_address")
	private String permanentaddress;

	@Column(name = "communication_address")
	private String communicationaddress;

	// Educational details
	@Column(name = "highest_qualification")
	private String highestqualification;

	@Column(name = "qualifying_branch")
	private String qualifyingbranch;

	@Column(name = "year_of_passing")
	private Long yearOfPassing;

	private String university;

	@Column(name = "college_address")
	private String collegeaddress;

	@Column(name = "technical_skills")
	private String technicalSkills;

	@Column(name = "technical_certification")
	private String technicalCertification;

	@Column(name = "cgpa_percentage")
	private Double cgpaPercentage;

	// Present employment details
	private Long salary;

	@Column(name = "manager_id")
	private String managerId;

	private String designation;

	private String department;

	@Column(name = "date_of_joining")
	private LocalDate dateOfJoining;

	@Column(name = "date_of_leaving")
	private LocalDate dateOfLeaving;

	// Previous employment details
	@Column(name = "years_experience")
	private Integer yearsexperience;

	@Column(name = "job_role")
	private String jobRole;

	@Column(name = "previous_company")
	private String previousCompany;

	@Column(name = "uan_number")
	private String uanNumber;

	@Column(name = "date_of_leaving_company")
	private LocalDate dateOfLeavingCompany;

	// Emergency contact
	@Column(name = "emergency_contact_person1")
	private String emergencyContactPerson1;

	@Column(name = "emergency_contact_person1_mobile")
	private String emergencyContactPerson1mobile;

	@Column(name = "emergency_contact_person1_email")
	private String emergencyContactPerson1email;

	@Column(name = "emergency_contact_person1_relation")
	private String emergencyContactPerson1relation;

	@Column(name = "emergency_contact_person2")
	private String emergencyContactPerson2;

	@Column(name = "emergency_contact_person2_mobile")
	private String emergencyContactPerson2mobile;

	@Column(name = "emergency_contact_person2_email")
	private String emergencyContactPerson2email;

	@Column(name = "emergency_contact_person2_relation")
	private String emergencyContactPerson2relation;

	// Login details
	private String role;

	private String password;

	private boolean status;
	
	@Column(name= "available_leaves")
	private Integer totalLeaves;
	
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ignore serialization of leaves here
    private List<Leaves> leaves;
}
