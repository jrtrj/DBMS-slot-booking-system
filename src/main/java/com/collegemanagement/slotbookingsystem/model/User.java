package com.collegemanagement.slotbookingsystem.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record User(
        Long id,
        String email,
        String hashedPassword,
        String firstName,
        String lastName,
        Role role,
        Long departmentId
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // The authority is the user's role. Spring Security requires the "ROLE_" prefix.
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        // We are using email as the unique identifier (username)
        return email;
    }

    // --- The following methods are for account status, which we aren't using yet. ---
    // --- We will return true for all of them to indicate active, enabled accounts. ---

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
