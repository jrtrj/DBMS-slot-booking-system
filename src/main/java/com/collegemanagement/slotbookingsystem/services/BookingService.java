package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.exceptions.ResourceNotFoundException;
import com.collegemanagement.slotbookingsystem.model.*;
import com.collegemanagement.slotbookingsystem.repository.department.DepartmentDao;
import com.collegemanagement.slotbookingsystem.repository.booking.BookingRequestDao;
import com.collegemanagement.slotbookingsystem.repository.user.UserDao;
import com.collegemanagement.slotbookingsystem.repository.venue.VenueDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BookingService {

    private final BookingRequestDao bookingRequestDao;
    private final VenueDao venueDao;
    private final UserDao userDao;
    private final DepartmentDao departmentDao;
    private final PasswordEncoder passwordEncoder;

    public BookingService(BookingRequestDao bookingRequestDao, VenueDao venueDao,
                          UserDao userDao, DepartmentDao departmentDao, PasswordEncoder passwordEncoder) {
        this.bookingRequestDao = bookingRequestDao;
        this.venueDao = venueDao;
        this.userDao = userDao;
        this.departmentDao = departmentDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The core logic for creating a new booking request.
     * @param requestData The JSON data from the controller
     * @param requester The user who is making the request
     * @return The newly created BookingRequest
     */
    @Transactional
    public BookingRequest createBookingRequest(Map<String, Object> requestData, User requester) {
        // 1. Extract data from the request map
        Long venueId = ((Number) requestData.get("venueId")).longValue();
        LocalDateTime startTime = LocalDateTime.parse((String) requestData.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((String) requestData.get("endTime"));
        Long forClubId = requestData.get("forClubId") != null ? ((Number) requestData.get("forClubId")).longValue() : null;
        Long forDeptId = requestData.get("forDepartmentId") != null ? ((Number) requestData.get("forDepartmentId")).longValue() : null;

        // 2. --- LOGIC: Check for existing approved conflicts ---
        List<BookingRequest> conflicts = bookingRequestDao.findApprovedConflicts(venueId, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Slot is already booked and approved");
        }

        // 3. --- SIMPLIFIED LOGIC: All requests go to the Principal for approval ---
        Long approverId = userDao.findByRole(Role.PRINCIPAL)
                .orElseThrow(() -> new ResourceNotFoundException("Principal user not found. Cannot assign approver."))
                .id();

        // Handle null requester for testing purposes
        Long requesterId;
        if (requester == null) {
            requesterId = -1L; // Use a dummy/sentinel ID for unauthenticated test requests
        } else {
            requesterId = requester.id();
        }

        // 4. Create the BookingRequest record
        BookingRequest newRequest = new BookingRequest(
                null, // id
                (String) requestData.get("eventTitle"),
                (String) requestData.get("eventDescription"),
                startTime,
                endTime,
                BookingStatus.PENDING, // Always starts as PENDING
                null, // rejectionReason
                requesterId,
                approverId,
                venueId,
                forClubId,
                forDeptId,
                null // createdAt (will be set by DB)
        );

        // 5. Save and return
        Long newId = bookingRequestDao.save(newRequest);
        return bookingRequestDao.findById(newId)
                .orElseThrow(() -> new IllegalStateException("Failed to create and retrieve booking request"));
    }

    /**
     * Retrieves all approved booking requests.
     * @return A list of approved bookings.
     */
    public List<BookingRequest> getApprovedBookings() {
        return bookingRequestDao.findApproved();
    }

    /**
     * Approves a booking request and rejects all conflicts.
     * @param requestId The ID of the request to approve
     * @param adminUser The admin user (HOD/Principal) performing the action
     */
    @Transactional
    public void approveBookingRequest(Long requestId, User adminUser) {
        BookingRequest request = bookingRequestDao.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request with ID " + requestId + " not found."));

        // --- AUTHORIZATION REMOVED ---
        // Any authenticated user can approve the request.

        // 1. Approve the request
        bookingRequestDao.updateStatus(requestId, BookingStatus.APPROVED, null);

        // 2. --- LOGIC: Auto-reject all other pending conflicts ---
        bookingRequestDao.rejectConflictingRequests(
                request.venueId(),
                request.startTime(),
                request.endTime(),
                requestId
        );
    }

    /**
     * Rejects a booking request.
     * @param requestId The ID of the request to reject
     * @param reason The reason for rejection
     * @param adminUser The admin user (HOD/Principal) performing the action
     */
    public void rejectBookingRequest(Long requestId, String reason, User adminUser) {
        BookingRequest request = bookingRequestDao.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request with ID " + requestId + " not found."));

        // --- AUTHORIZATION REMOVED ---
        // Any authenticated user can reject the request.

        bookingRequestDao.updateStatus(requestId, BookingStatus.REJECTED, reason);
    }

    /**
     * Gets the dashboard of pending requests for an admin.
     * This is where the priority logic will be added.
     * @param adminUser The HOD or Principal
     * @return A list of pending requests
     */
    public List<BookingRequest> getPendingBookingsForAdmin(User adminUser) {
        // --- SIMPLIFIED LOGIC ---
        // Return all pending requests, regardless of the user's role or who the approver is.
        // Note: This would require a new `findAllPending` method in the DAO.
        // For now, we can reuse `findPendingForPrincipal` as a stand-in since we made it the default.
        return bookingRequestDao.findPendingForPrincipal();
    }
}
