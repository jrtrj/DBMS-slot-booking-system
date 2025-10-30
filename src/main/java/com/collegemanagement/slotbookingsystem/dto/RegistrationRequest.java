package com.collegemanagement.slotbookingsystem.dto;

/**
 * A Data Transfer Object (DTO) representing the data required to register a new user.
 * This provides type safety and clear validation contracts.
 */
public class RegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private Long departmentId;

    //<editor-fold desc="Getters and Setters">
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    //</editor-fold>
}