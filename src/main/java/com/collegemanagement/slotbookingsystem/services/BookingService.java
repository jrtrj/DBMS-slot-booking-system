package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.model.*;
import com.collegemanagement.slotbookingsystem.repository.department.DepartmentDao;
import com.collegemanagement.slotbookingsystem.repository.booking.BookingRequestDao;
import com.collegemanagement.slotbookingsystem.repository.user.UserDao;
import com.collegemanagement.slotbookingsystem.repository.venue.VenueDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BookingService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We use email as the username
        return userDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
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

        // 3. --- LOGIC: Determine the correct approver ---
        Venue venue = venueDao.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        Long approverId;
        if (Objects.equals(venue.departmentId(), requester.departmentId())) {
            // INTERNAL: Belongs to the user's own department. Get the HOD.
            Department dept = departmentDao.findById(requester.departmentId())
                    .orElseThrow(() -> new RuntimeException("Requester's department not found"));
            approverId = dept.hodId();
            if (approverId == null) {
                throw new RuntimeException("HOD for this department is not set");
            }
        } else {
            // EXTERNAL: Belongs to another department. Get the Principal.
            approverId = userDao.findByRole(Role.PRINCIPAL)
                    .orElseThrow(() -> new RuntimeException("Principal user not found"))
                    .id();
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
                requester.id(),
                approverId,
                venueId,
                forClubId,
                forDeptId,
                null // createdAt (will be set by DB)
        );

        // 5. Save and return
        Long newId = bookingRequestDao.save(newRequest);
        return bookingRequestDao.findById(newId)
                .orElseThrow(() -> new RuntimeException("Failed to create and retrieve booking request"));
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
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        // --- LOGIC: Check authorization ---
        if (!Objects.equals(request.approverId(), adminUser.id())) {
            // A special check for the Principal, who can override
            if (adminUser.role() != Role.PRINCIPAL) {
                throw new RuntimeException("You are not authorized to approve this request");
            }
        }

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
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        // --- LOGIC: Check authorization ---
        if (!Objects.equals(request.approverId(), adminUser.id())) {
            if (adminUser.role() != Role.PRINCIPAL) {
                throw new RuntimeException("You are not authorized to reject this request");
            }
        }

        bookingRequestDao.updateStatus(requestId, BookingStatus.REJECTED, reason);
    }

    /**
     * Gets the dashboard of pending requests for an admin.
     * This is where the priority logic will be added.
     * @param adminUser The HOD or Principal
     * @return A list of pending requests
     */
    public List<BookingRequest> getPendingBookingsForAdmin(User adminUser) {
        List<BookingRequest> pendingRequests;
        if (adminUser.role() == Role.HOD) {
            // HODs see requests assigned to them
            pendingRequests = bookingRequestDao.findPendingForApprover(adminUser.id());
        } else if (adminUser.role() == Role.PRINCIPAL) {
            // Principals see requests assigned to them (external)
            pendingRequests = bookingRequestDao.findPendingForPrincipal();
            // Principals might also see requests directly assigned to them (e.g., for main hall)
            pendingRequests.addAll(bookingRequestDao.findPendingForApprover(adminUser.id()));
        } else {
            throw new RuntimeException("User is not an admin");
        }

        // --- LOGIC: Add priority data here ---
        // We will loop through `pendingRequests` and attach the counts from the DAO
        // For now, we return the simple list.

        return pendingRequests;
    }
}
