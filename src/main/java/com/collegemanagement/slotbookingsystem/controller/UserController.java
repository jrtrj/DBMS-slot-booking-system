package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles HTTP POST requests to /api/users/register.
     * Creates a new user.
     *
     * @param requestBody A JSON object with user details.
     * @return The newly created user (as JSON) and a 201 Created status.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody Map<String, Object> requestBody) {
        return userService.registerUser(requestBody);
    }

    /**
     * Handles HTTP GET requests to /api/users.
     * Fetches and returns a list of all users.
     *
     * @return A list of all users (as JSON).
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}