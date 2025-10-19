package com.collegemanagement.slotbookingsystem.model;

public record Venue(
		Long id,
		String name,
		String block,
		int capacity,
		Long departmentId
) {}
