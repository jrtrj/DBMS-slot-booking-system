# Slot Management System

A System for managing and booking college venues like seminar halls, labs, and auditoriums. This system automates the booking approval process for students, clubs, and departments.
## Core Features
 * REST API: Provides endpoints for viewing venues, requesting bookings, and admin actions.
 * Role-Based Access: Handles different user roles (Student, HOD, Principal, Admin).
 * Dynamic Approval Workflow: Automatically routes booking requests to the correct approver (HOD for internal requests, Principal for external requests).
 * Booking Priority Logic: Provides admins with data to fairly manage conflicting booking requests.

## Technology Stack
 * Java 21
 * Spring Boot
 * Spring JDBC (using raw SQL)
 * Spring Security
 * MariaDB
 * Maven
 * Docker / Docker Compose
