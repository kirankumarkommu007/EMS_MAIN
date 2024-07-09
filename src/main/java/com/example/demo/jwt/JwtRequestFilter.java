package com.example.demo.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger loggers = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;
    
    
    public JwtRequestFilter( UserDetailsService userDetailsService,JwtUtil jwtUtil) {
    	this.userDetailsService = userDetailsService;
    	this.jwtUtil= jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwtToken = null;
        String username = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    jwtToken = cookie.getValue();
                    loggers.info("JWT Token found in cookies");
                }
            }
        }

        if (jwtToken != null) {
            try {
                username = jwtUtil.extractUsername(jwtToken);
                loggers.info("Extracted username from JWT Token: {}", username);
            } catch (IllegalArgumentException e) {
            	loggers.error("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                loggers.warn("JWT Token has expired", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            boolean isTokenValid = jwtUtil.validateToken(jwtToken, userDetails);
            if (isTokenValid) {
                Claims claims = jwtUtil.extractAllClaims(jwtToken);
                List<String> roles = claims.get("roles", List.class);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                loggers.info("Authenticated user: {}", username);
            }

            
        }
        chain.doFilter(request, response);
    }
}
