package com.collegemanagement.slotbookingsystem.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.collegemanagement.slotbookingsystem.repository.venue.VenueDao;
import com.collegemanagement.slotbookingsystem.model.Venue;

@Service
public class VenueService {
	private final VenueDao venueDao;

	public VenueService(VenueDao venueDao) {
		this.venueDao = venueDao;
	}
	
	public List<Venue> getAllVenues() {
		return venueDao.findAll();
	}
}