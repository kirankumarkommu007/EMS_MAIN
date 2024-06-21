package com.example.demo.security;

import com.example.demo.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MyUserDetailsService employeeDetailsServiceImpl;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                logger.info("Setting authorization rules");
                auth
                    .requestMatchers("/login", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/hello").permitAll()
                    .requestMatchers("/admin/**", "/updateRole/**").hasRole("ADMIN")
                    .requestMatchers("/listemployees", "/activeEmployees", "/updateStatus").hasAnyRole("ADMIN", "HR")
                    .requestMatchers("/hr/**").hasRole("HR")
                    .requestMatchers("/user/**").hasRole("USER")
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
        logger.info("Configured authentication provider");
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Configuring password encoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        logger.info("Configuring authentication manager");
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(employeeDetailsServiceImpl).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
