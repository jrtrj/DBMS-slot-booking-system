package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.repository.user.UserDao;
import com.collegemanagement.slotbookingsystem.model.Role;
import com.collegemanagement.slotbookingsystem.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Handles all business logic related to Users, including
 * registration, authentication, and validation.
 */
@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    // We inject both the DAO and the PasswordEncoder
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    /**
     * Registers a new user.
     * This is where our core password-hashing logic lives.
     * @param userData A map containing user details
     * @return The newly created User object.
     */
    public User registerUser(Map<String, Object> userData) {
        String email = (String) userData.get("email");
        String plainTextPassword = (String) userData.get("password");

        // Validation Logic
        if (userDao.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(plainTextPassword);
        // Create the new User record
        User newUser = new User(
                null, // id is null because it will be auto-generated
                email,
                hashedPassword,
                (String) userData.get("firstName"),
                (String) userData.get("lastName"),
                Role.valueOf((String) userData.get("role")), // e.g., "STUDENT"
                ((Number) userData.get("departmentId")).longValue()
        );

        // Save to database and get the new ID
        Long newId = userDao.save(newUser);

        return userDao.findById(newId).orElseThrow(() ->
                new RuntimeException("Failed to create and retrieve user")
        );
    }

}
