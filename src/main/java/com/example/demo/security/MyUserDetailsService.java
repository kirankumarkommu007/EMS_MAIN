package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

	private final EmployeeRepo empRepo;

	public MyUserDetailsService(EmployeeRepo empRepo) {
		this.empRepo = empRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Loading user by username: {}", username);
		Employees emp = empRepo.findByEmployeeId(username);
		if (emp == null) {
			logger.error("User not found with username: {}", username);
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		logger.warn("User found: {}", username);
		return org.springframework.security.core.userdetails.User.builder().username(emp.getEmployeeId())
				.password(emp.getPassword()).roles(emp.getRole()).build();
	}
}
