package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.dto.UserResponseDTO;
import com.collegemanagement.slotbookingsystem.dto.LoginRequest;
import com.collegemanagement.slotbookingsystem.dto.RegistrationRequest;
import com.collegemanagement.slotbookingsystem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "localhost:5173")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequest loginRequest) {
        // @RequestBody converts the JSON request body into a LoginRequest object
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(UserResponseDTO.fromUser(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegistrationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserResponseDTO::fromUser)
                .collect(java.util.stream.Collectors.toList());
    }
}
