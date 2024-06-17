package com.example.demo.models;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "employees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Personal details
    private String firstname;
    private String middlename;
    private String lastname;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    private String bloodgroup;
    private String gender;
    
    @Column(unique = true, nullable = false)
    private String pan;
    @Column(unique = true, nullable = false)
    private String adhaar;
    @Column(unique = true, nullable = false)
    private Long mobile;
    @Column(unique = true, nullable = false)
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
    private Long emergencyContactPerson1mobile;
    
    @Column(name = "emergency_contact_person1_email")
    private String emergencyContactPerson1email;
    
    @Column(name = "emergency_contact_person1_relation")
    private String emergencyContactPerson1relation;

    @Column(name = "emergency_contact_person2")
    private String emergencyContactPerson2;
    
    @Column(name = "emergency_contact_person2_mobile")
    private Long emergencyContactPerson2mobile;
    
    @Column(name = "emergency_contact_person2_email")
    private String emergencyContactPerson2email;
    
    @Column(name = "emergency_contact_person2_relation")
    private String emergencyContactPerson2relation;

    // Login details
    private String role;
    private String password;
    private boolean status;
}
