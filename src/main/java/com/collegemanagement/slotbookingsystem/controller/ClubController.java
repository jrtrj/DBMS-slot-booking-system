package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.Club;
import com.collegemanagement.slotbookingsystem.dto.CreateClubRequest;
import com.collegemanagement.slotbookingsystem.services.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles all incoming HTTP requests related to clubs.
 */
@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * Handles HTTP GET requests to /api/clubs.
     * Fetches and returns a list of all clubs.
     */
    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    /**
     * Handles HTTP GET requests to /api/clubs/{id}.
     * Fetches a single club by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        return clubService.getClubById(id)
                .map(club -> ResponseEntity.ok(club)) // 200 OK
                .orElse(ResponseEntity.notFound().build());      // 404 Not Found
    }

    /**
     * Handles HTTP POST requests to /api/clubs.
     * Creates a new club.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Club createClub(@RequestBody CreateClubRequest request) {
        return clubService.createClub(request.getName());
    }
}