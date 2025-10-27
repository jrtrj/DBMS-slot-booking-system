package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.dao.ClubDao;
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

    /**
     * Gets a list of all clubs.
     * @return List of all clubs.
     */
    public List<Club> getAllClubs() {
        return clubDao.findAll();
    }

    /**
     * Gets a single club by its ID.
     * @param id The ID of the club.
     * @return An Optional containing the club if found.
     */
    public Optional<Club> getClubById(Long id) {
        return clubDao.findById(id);
    }

    /**
     * Creates a new club.
     * @param clubName The name for the new club.
     * @return The newly created Club object.
     */
    public Club createClub(String clubName) {
        // 1. Save the new club and get its new ID
        Long newId = clubDao.save(clubName);

        // 2. Return the complete Club object
        return clubDao.findById(newId).orElseThrow(() ->
                new RuntimeException("Failed to create and retrieve club")
        );
    }
}
