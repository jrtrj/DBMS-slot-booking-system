package com.collegemanagement.slotbookingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Cross-Site Request Forgery)
            // We can disable this for now as we are building a stateless REST API.
            .csrf(csrf -> csrf.disable())
            
            // 2. Configure Authorization Rules
            .authorizeHttpRequests(authz -> authz
                // This line tells Spring Security to allow ALL requests to ALL endpoints.
                .requestMatchers("/**").permitAll() 
            )
            
            // 3. Disable the default login page
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}