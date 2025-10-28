package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.User;
import com.collegemanagement.slotbookingsystem.dto.LoginRequest;
import com.collegemanagement.slotbookingsystem.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }
}
