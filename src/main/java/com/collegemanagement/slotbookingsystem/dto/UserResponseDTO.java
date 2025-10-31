package com.collegemanagement.slotbookingsystem.dto;

import com.collegemanagement.slotbookingsystem.model.Role;
import com.collegemanagement.slotbookingsystem.model.User;

public class UserResponseDTO {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final Long departmentId;

    public UserResponseDTO(Long id, String email, String firstName, String lastName, Role role, Long departmentId) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.departmentId = departmentId;
    }

    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.id(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.role(),
                user.departmentId()
        );
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
    public Long getDepartmentId() { return departmentId; }
}