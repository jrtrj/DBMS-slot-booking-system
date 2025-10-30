package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.dto.RegistrationRequest;
import com.collegemanagement.slotbookingsystem.model.Role;
import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.repository.user.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We use email as the username
        return userDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User registerUser(RegistrationRequest request) {
        // Add validation for email, password, etc.
        if (userDao.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User userToSave = new User(
                null, // ID is auto-generated
                request.getEmail(),
                hashedPassword,
                request.getFirstName(),
                request.getLastName(),
                Role.valueOf(request.getRole().toUpperCase()),
                request.getDepartmentId()
        );

        Long id = userDao.save(userToSave);
        return userDao.findById(id).orElseThrow(() -> new IllegalStateException("Could not retrieve user after creation."));
    }

    /**
     * Authenticates a user based on their email and password.
     * @param email The user's email.
     * @param plainTextPassword The user's plain text password.
     * @return The authenticated User object if successful.
     * @throws RuntimeException if authentication fails.
     */
    public User login(String email, String plainTextPassword) { // 1. Find the user by their email
        User user = userDao.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email or password"));
        // 2. Check if the provided password matches the stored hash
        if (passwordEncoder.matches(plainTextPassword, user.hashedPassword())) {
            // 3. Passwords match! Return the user.
            return user;
        } else {
            // 4. Passwords do not match.
            throw new RuntimeException("Invalid email or password");
        }
    }
}
