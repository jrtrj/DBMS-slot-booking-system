package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.repository.club.ClubDao;
import com.collegemanagement.slotbookingsystem.model.Club;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles all business logic related to Clubs.
 */
@Service
public class ClubService {
    private final ClubDao clubDao;

    public ClubService(ClubDao clubDao) {
        this.clubDao = clubDao;
    }

    public List<Club> getAllClubs() {
        return clubDao.findAll();
    }

    public Optional<Club> getClubById(Long id) {
        return clubDao.findById(id);
    }

    public Club createClub(String clubName) {
        // 1. Save the new club and get its new ID
        Long newId = clubDao.save(clubName);

        // 2. Return the complete Club object
        return clubDao.findById(newId).orElseThrow(() ->
                new RuntimeException("Failed to create and retrieve club")
        );
    }
}
