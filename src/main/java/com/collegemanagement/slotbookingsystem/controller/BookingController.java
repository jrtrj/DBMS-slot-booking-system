package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.BookingRequest;
import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.repository.user.UserDao;
import com.collegemanagement.slotbookingsystem.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    // --- TEMPORARY: For testing only ---
    // We inject UserDao to fetch mock users.
    // This will be replaced by Spring Security's AuthenticationPrincipal.
    private final UserDao userDao;
    // ---

    public BookingController(BookingService bookingService, UserDao userDao) {
        this.bookingService = bookingService;
        this.userDao = userDao; // TEMPORARY
    }

    // --- MOCK USER HELPER (TEMPORARY) ---
    private User getMockStudent() {
        // Fetches user with ID 1. Assumes this is a STUDENT.
        // Make sure you have created a student with ID 1 in your database!
        return userDao.findById(1L).orElseThrow(() -> new RuntimeException("Mock Student (ID 1) not found"));
    }

    private User getMockAdmin() {
        // Fetches user with ID 2. Assumes this is an HOD or PRINCIPAL.
        // Make sure you have created an admin (HOD) with ID 2 in your database!
        return userDao.findById(2L).orElseThrow(() -> new RuntimeException("Mock Admin (ID 2) not found"));
    }
    // ---

    /**
     * Handles POST requests to /api/bookings/request
     * A user (student/teacher) creates a new booking request.
     */
    @PostMapping("/request")
    public ResponseEntity<BookingRequest> createBookingRequest(@RequestBody Map<String, Object> requestData) {
        // TEMPORARY: We are using a mock student user
        User studentUser = getMockStudent();

        BookingRequest newRequest = bookingService.createBookingRequest(requestData, studentUser);
        return ResponseEntity.status(201).body(newRequest);
    }

    /**
     * Handles GET requests to /api/bookings/approved
     * Fetches a list of all approved booking requests.
     */
    @GetMapping("/approved")
    public List<BookingRequest> getApprovedBookings() {
        return bookingService.getApprovedBookings();
    }

    /**
     * Handles POST requests to /api/bookings/approve/{id}
     * An admin (HOD/Principal) approves a request.
     */
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveBookingRequest(@PathVariable Long id) {
        // TEMPORARY: We are using a mock admin user
        User adminUser = getMockAdmin();

        bookingService.approveBookingRequest(id, adminUser);
        return ResponseEntity.ok("Booking request " + id + " approved.");
    }

    /**
     * Handles POST requests to /api/bookings/reject/{id}
     * An admin (HOD/Principal) rejects a request.
     */
    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectBookingRequest(@PathVariable Long id, @RequestBody Map<String, String> body) {
        // TEMPORARY: We are using a mock admin user
        User adminUser = getMockAdmin();
        String reason = body.get("reason");

        bookingService.rejectBookingRequest(id, reason, adminUser);
        return ResponseEntity.ok("Booking request " + id + " rejected.");
    }

    /**
     * Handles GET requests to /api/bookings/pending
     * An admin (HOD/Principal) views their pending dashboard.
     */
    @GetMapping("/pending")
    public List<BookingRequest> getPendingBookings() {
        // TEMPORARY: We are using a mock admin user
        User adminUser = getMockAdmin();

        return bookingService.getPendingBookingsForAdmin(adminUser);
    }
}