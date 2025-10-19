package com.collegemanagement.slotbookingsystem.model;
import java.time.LocalDateTime;

public record BookingRequest(
    Long id,
    String eventTitle,
    String eventDescription,
    LocalDateTime startTime,
    LocalDateTime endTime,
    BookingStatus status,
    String rejectionReason,
    Long requesterId,
    Long approverId,
    Long venueId,
    Long forClubId,
    Long forDepartmentId,
    LocalDateTime createdAt
) {}