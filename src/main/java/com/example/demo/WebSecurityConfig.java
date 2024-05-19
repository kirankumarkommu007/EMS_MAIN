package com.example.demo;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public MyUserDetailsService myUserDetailsService;

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/hr/**").hasRole("HR") 
                .requestMatchers("/user/**").hasRole("USER") 
                .anyRequest().authenticated()
            )
//            .oauth2Login(withDefaults())
            .formLogin((form) -> form
                .loginPage("/login") 
                .usernameParameter("username") 
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    if (authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/home");
                    } else if (authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_HR"))) {
                        response.sendRedirect("/hr/home");
                    } else {
                        response.sendRedirect("/user/profile");
                    }
                })
                .permitAll()
            )
 
            .logout((logout) -> logout
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                );
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


	   @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(myUserDetailsService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }


//	@Bean
//	public UserDetailsService userDetailsService() {
//
//		UserDetails hr = User.builder()
//				.username("hr")
//				.password(passwordEncoder().encode("1234"))
//				.roles("HR")
//				.build();
//
//		UserDetails admin = User.builder()
//				.username("admin")
//				.password(passwordEncoder().encode("kiran@1226"))
//				.roles("ADMIN")
//				.build();
//
//
//		return new InMemoryUserDetailsManager(hr,admin );
//
//
//	}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    }
