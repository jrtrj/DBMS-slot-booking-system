package com.collegemanagement.slotbookingsystem.dto;

/**
 * DTO for creating a new department.
 */
public class CreateDepartmentRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}