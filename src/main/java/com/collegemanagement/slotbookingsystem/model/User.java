package com.collegemanagement.slotbookingsystem.model;

public record User(
	Long id,
	String email,
	String hashedPassword,
	String firstName,
	String lastName,
	Role role,
	Long departmentId
){}
