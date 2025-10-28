package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.BookingRequest;
import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Handles POST requests to /api/bookings/request
     * A user (student/teacher) creates a new booking request.
     * The logged-in user is automatically passed by Spring Security.
     */
    @PostMapping("/request")
    public ResponseEntity<BookingRequest> createBookingRequest(
            @RequestBody Map<String, Object> requestData,
            @AuthenticationPrincipal User user) {
        BookingRequest newRequest = bookingService.createBookingRequest(requestData, user);
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
    public ResponseEntity<String> approveBookingRequest(@PathVariable Long id, @AuthenticationPrincipal User adminUser) {
        bookingService.approveBookingRequest(id, adminUser);
        return ResponseEntity.ok("Booking request " + id + " approved.");
    }

    /**
     * Handles POST requests to /api/bookings/reject/{id}
     * An admin (HOD/Principal) rejects a request.
     */
    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectBookingRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User adminUser) {
        String reason = body.get("reason");
        bookingService.rejectBookingRequest(id, reason, adminUser);
        return ResponseEntity.ok("Booking request " + id + " rejected.");
    }

    /**
     * Handles GET requests to /api/bookings/pending
     * An admin (HOD/Principal) views their pending dashboard.
     */
    @GetMapping("/pending")
    public List<BookingRequest> getPendingBookings(@AuthenticationPrincipal User adminUser) {
        return bookingService.getPendingBookingsForAdmin(adminUser);
    }
}
