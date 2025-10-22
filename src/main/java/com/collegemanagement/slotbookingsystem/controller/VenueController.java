package com.collegemanagement.slotbookingsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collegemanagement.slotbookingsystem.model.Venue;
import com.collegemanagement.slotbookingsystem.services.VenueService;

@RestController
@RequestMapping("/api/venues")

public class VenueController {
	private VenueService venueService;
	
	public VenueController(VenueService venueService) {
		this.venueService = venueService;
	}
	@GetMapping
	public List<Venue> getVenues() {
		return venueService.getAllVenues();
	}
}