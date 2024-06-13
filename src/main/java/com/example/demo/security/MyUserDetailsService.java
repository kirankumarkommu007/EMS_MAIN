package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.Employees;
import com.example.demo.repos.EmployeeRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepo empRepo;
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employees emp = empRepo.findByFirstname(username);
        if (emp == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(emp.getFirstname())
                .password(emp.getPassword())
                .roles(emp.getRole())
                .build();
    }
}
