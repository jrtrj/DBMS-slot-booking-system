package com.collegemanagement.slotbookingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection for stateless APIs
            .csrf(csrf -> csrf.disable())

            // Define authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow anyone to attempt to log in
                .requestMatchers("/api/users/login").permitAll()

                // --- Booking Requests ---
                // Any authenticated user can create a request or view approved bookings
                .requestMatchers(HttpMethod.POST, "/api/bookings/request").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/bookings/approved").authenticated()
                // Only admins (HOD, PRINCIPAL) can approve, reject, or view pending requests
                .requestMatchers("/api/bookings/approve/**", "/api/bookings/reject/**", "/api/bookings/pending").hasAnyRole("HOD", "PRINCIPAL")

                // --- Admin-Only Endpoints ---
                // Only a SUPERADMIN can view all users or departments (as an example)
                .requestMatchers("/api/users", "/api/departments").hasRole("SUPERADMIN")

                // By default, deny all other requests
                .anyRequest().denyAll()
            )

            // Enable HTTP Basic Authentication
            .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
