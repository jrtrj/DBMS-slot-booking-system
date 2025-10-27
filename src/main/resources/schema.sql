-- Select your database first
USE college_slots;

-- Temporarily disable foreign key checks to drop tables with circular dependencies
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables in a single statement
DROP TABLE IF EXISTS 
    booking_requests, 
    user_clubs, 
    venues, 
    users, 
    clubs, 
    departments;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Clubs Table
CREATE TABLE IF NOT EXISTS clubs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 3. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role ENUM('STUDENT', 'TEACHER', 'HOD', 'PRINCIPAL', 'SUPERADMIN') NOT NULL,
    department_id BIGINT,
    
    -- Corrected Foreign Key
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Alter tables to add HOD and Advisor foreign keys after `users` table is created
ALTER TABLE departments
ADD COLUMN IF NOT EXISTS hod_id BIGINT UNIQUE,
ADD CONSTRAINT fk_department_hod
    FOREIGN KEY (hod_id) REFERENCES users(id)
    ON DELETE SET NULL; -- Allows hod_id to be NULL

ALTER TABLE clubs
ADD COLUMN IF NOT EXISTS faculty_advisor_id BIGINT,
ADD CONSTRAINT fk_club_advisor
    FOREIGN KEY (faculty_advisor_id) REFERENCES users(id)
    ON DELETE SET NULL;

-- 4. NEW: user_clubs (Junction Table)
CREATE TABLE IF NOT EXISTS user_clubs (
    user_id BIGINT NOT NULL,
    club_id BIGINT NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    
    PRIMARY KEY (user_id, club_id)
);

-- 5. Venues Table
CREATE TABLE IF NOT EXISTS venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    block VARCHAR(100),
    capacity INT NOT NULL DEFAULT 0,
    department_id BIGINT NOT NULL, 
    
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

-- 6. Booking Requests Table
CREATE TABLE IF NOT EXISTS booking_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_title VARCHAR(255) NOT NULL,
    event_description TEXT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    rejection_reason TEXT,
    requester_id BIGINT NOT NULL,
    approver_id BIGINT,
    venue_id BIGINT NOT NULL,
    for_club_id BIGINT,
    for_department_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (requester_id) REFERENCES users(id),
    FOREIGN KEY (approver_id) REFERENCES users(id),
    FOREIGN KEY (venue_id) REFERENCES venues(id),
    FOREIGN KEY (for_club_id) REFERENCES clubs(id),
    -- Corrected table name typo
    FOREIGN KEY (for_department_id) REFERENCES departments(id), 
    
    INDEX idx_venue_time (venue_id, start_time, end_time)
);