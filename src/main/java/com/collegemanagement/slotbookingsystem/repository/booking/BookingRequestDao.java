package com.collegemanagement.slotbookingsystem.repository.booking;

import com.collegemanagement.slotbookingsystem.model.BookingRequest;
import com.collegemanagement.slotbookingsystem.model.BookingStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRequestDao {

    private final JdbcTemplate jdbcTemplate;
    private final BookingRequestRowMapper rowMapper = new BookingRequestRowMapper();

    public BookingRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * READ: Finds a single booking request by its unique ID.
     */
    public Optional<BookingRequest> findById(Long id) {
        String sql = "SELECT * FROM booking_requests WHERE id = ?";
        try {
            BookingRequest request = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(request);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * READ: Finds all booking requests with an 'APPROVED' status.
     */
    public List<BookingRequest> findApproved() {
        String sql = "SELECT * FROM booking_requests WHERE status = 'APPROVED'";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * CREATE: Saves a new booking request to the database.
     * @return The auto-generated ID of the new request.
     */
    public Long save(BookingRequest request) {
        String sql = "INSERT INTO booking_requests (event_title, event_description, start_time, end_time, " +
                "status, requester_id, approver_id, venue_id, for_club_id, for_department_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.eventTitle());
            ps.setString(2, request.eventDescription());
            ps.setTimestamp(3, Timestamp.valueOf(request.startTime()));
            ps.setTimestamp(4, Timestamp.valueOf(request.endTime()));
            ps.setString(5, request.status().name());
            ps.setLong(6, request.requesterId());

            // Handle optional approver_id
            if (request.approverId() != null) {
                ps.setLong(7, request.approverId());
            } else {
                ps.setNull(7, java.sql.Types.BIGINT);
            }

            ps.setLong(8, request.venueId());

            // Handle optional for_club_id
            if (request.forClubId() != null) {
                ps.setLong(9, request.forClubId());
            } else {
                ps.setNull(9, java.sql.Types.BIGINT);
            }

            // Handle optional for_department_id
            if (request.forDepartmentId() != null) {
                ps.setLong(10, request.forDepartmentId());
            } else {
                ps.setNull(10, java.sql.Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /**
     * UPDATE: Changes the status of a booking request (e.g., to APPROVED or REJECTED).
     */
    public int updateStatus(Long id, BookingStatus status, String rejectionReason) {
        String sql = "UPDATE booking_requests SET status = ?, rejection_reason = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status.name(), rejectionReason, id);
    }

    /**
     * LOGIC: Finds any *approved* bookings that overlap with a given time for a specific venue.
     * This is the core conflict-checking query.
     */
    public List<BookingRequest> findApprovedConflicts(Long venueId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM booking_requests " +
                "WHERE venue_id = ? " +
                "AND status = 'APPROVED' " +
                "AND ( (start_time < ?) AND (end_time > ?) )"; // Checks for overlaps

        return jdbcTemplate.query(sql, rowMapper, venueId, endTime, startTime);
    }

    /**
     * LOGIC: Finds pending requests for a specific approver.
     * This is for the admin's dashboard.
     */
    public List<BookingRequest> findPendingForApprover(Long approverId) {
        String sql = "SELECT * FROM booking_requests WHERE approver_id = ? AND status = 'PENDING'";
        return jdbcTemplate.query(sql, rowMapper, approverId);
    }

    /**
     * LOGIC: Finds pending requests for the Principal (requests outside the user's dept).
     */
    public List<BookingRequest> findPendingForPrincipal() {
        // This query joins users and venues to compare their department_id
        String sql = "SELECT br.* FROM booking_requests br " +
                "JOIN users u ON br.requester_id = u.id " +
                "JOIN venues v ON br.venue_id = v.id " +
                "JOIN users principal ON br.approver_id = principal.id " +
                "WHERE br.status = 'PENDING' " +
                "AND principal.role = 'PRINCIPAL' " +
                "AND u.department_id != v.department_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * LOGIC: Counts bookings for a group (club or dept) in the current week.
     * This is for the priority logic.
     */
    public int countBookingsForGroupThisWeek(Long clubId, Long deptId, LocalDateTime now) {
        String sql = "SELECT COUNT(*) FROM booking_requests " +
                "WHERE (for_club_id = ? OR for_department_id = ?) " +
                "AND status = 'APPROVED' " +
                "AND WEEK(start_time, 1) = WEEK(?, 1) " + // 1 = Week starts on Monday
                "AND YEAR(start_time) = YEAR(?)";

        Long club = (clubId != null) ? clubId : -1L; // Use -1 if null so it doesn't match
        Long dept = (deptId != null) ? deptId : -1L;

        return jdbcTemplate.queryForObject(sql, Integer.class, club, dept, Timestamp.valueOf(now), Timestamp.valueOf(now));
    }

    /**
     * LOGIC: Counts bookings for a group (club or dept) today.
     * This is for the priority logic.
     */
    public int countBookingsForGroupToday(Long clubId, Long deptId, LocalDateTime now) {
        String sql = "SELECT COUNT(*) FROM booking_requests " +
                "WHERE (for_club_id = ? OR for_department_id = ?) " +
                "AND status = 'APPROVED' " +
                "AND DATE(start_time) = DATE(?)";

        Long club = (clubId != null) ? clubId : -1L;
        Long dept = (deptId != null) ? deptId : -1L;

        return jdbcTemplate.queryForObject(sql, Integer.class, club, dept, Timestamp.valueOf(now));
    }

    /**
     * LOGIC: Rejects all other pending requests that conflict with a newly approved one.
     */
    public int rejectConflictingRequests(Long venueId, LocalDateTime startTime, LocalDateTime endTime, Long approvedRequestId) {
        String sql = "UPDATE booking_requests SET status = 'REJECTED', rejection_reason = 'Slot conflict with approved event' " +
                "WHERE venue_id = ? " +
                "AND status = 'PENDING' " +
                "AND id != ? " + // Don't reject the one we just approved
                "AND ( (start_time < ?) AND (end_time > ?) )";

        return jdbcTemplate.update(sql, venueId, approvedRequestId, endTime, startTime);
    }
}
