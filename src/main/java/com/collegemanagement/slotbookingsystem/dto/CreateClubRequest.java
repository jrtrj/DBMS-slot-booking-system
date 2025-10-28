package com.collegemanagement.slotbookingsystem.dto;

/**
 * DTO for creating a new club.
 */
public class CreateClubRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}