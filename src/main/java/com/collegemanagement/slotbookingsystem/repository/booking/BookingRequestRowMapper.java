package com.collegemanagement.slotbookingsystem.repository.booking;

import com.collegemanagement.slotbookingsystem.model.BookingRequest;
import com.collegemanagement.slotbookingsystem.model.BookingStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Maps a row from the 'booking_requests' table to a BookingRequest record.
 */
public class BookingRequestRowMapper implements RowMapper<BookingRequest> {

    @Override
    public BookingRequest mapRow(ResultSet rs, int rowNum) throws SQLException {

        // Helper to safely retrieve Long values that might be NULL
        Long approverId = rs.getObject("approver_id") != null ? rs.getLong("approver_id") : null;
        Long forClubId = rs.getObject("for_club_id") != null ? rs.getLong("for_club_id") : null;
        Long forDeptId = rs.getObject("for_department_id") != null ? rs.getLong("for_department_id") : null;

        return new BookingRequest(
                rs.getLong("id"),
                rs.getString("event_title"),
                rs.getString("event_description"),
                rs.getObject("start_time", LocalDateTime.class),
                rs.getObject("end_time", LocalDateTime.class),
                BookingStatus.valueOf(rs.getString("status")),
                rs.getString("rejection_reason"),
                rs.getLong("requester_id"),
                approverId,
                rs.getLong("venue_id"),
                forClubId,
                forDeptId,
                rs.getObject("created_at", LocalDateTime.class)
        );
    }
}