package com.example.demo.security;

import com.example.demo.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final MyUserDetailsService employeeDetailsServiceImpl;


    private final JwtRequestFilter jwtRequestFilter;
    
    public SecurityConfig( MyUserDetailsService employeeDetailsServiceImpl,JwtRequestFilter jwtRequestFilter ) {
    	this.employeeDetailsServiceImpl =employeeDetailsServiceImpl;
    	this.jwtRequestFilter =jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                logger.info("Setting authorization rules");
                auth
                .requestMatchers("/login", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/hello", "/simplepage", "/sendEmail", "/send-sms").permitAll()
                .requestMatchers("/listemployees", "/activeEmployees", "/updateStatus", "/updateRole/**", "/approve", "/deny","/pending").hasAnyRole("ADMIN", "HR")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/hr/**").hasRole("HR")
                .anyRequest().authenticated();
            })
            .formLogin(form -> form
                .loginPage("/welcome")
                .failureUrl("/login?error=true").permitAll())
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies("token", "JSESSIONID"))
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(employeeDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Configuring password encoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(employeeDetailsServiceImpl).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
