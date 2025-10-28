package com.collegemanagement.slotbookingsystem.controller;

import com.collegemanagement.slotbookingsystem.model.Department;
import com.collegemanagement.slotbookingsystem.dto.CreateDepartmentRequest;
import com.collegemanagement.slotbookingsystem.services.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Handles HTTP GET requests to /api/departments.
     * Fetches and returns a list of all departments.
     *
     * @return A list of all departments (as JSON).
     */
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * Handles HTTP GET requests to /api/departments/{id}.
     * Fetches a single department by its ID.
     *
     * @param id The ID of the department, taken from the URL path.
     * @return The department (as JSON) or a 404 Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(department -> ResponseEntity.ok(department)) // 200 OK
                .orElse(ResponseEntity.notFound().build());      // 404 Not Found
    }

    /**
     * Handles HTTP POST requests to /api/departments.
     * Creates a new department.
     *
     * @param request A JSON object, e.g., { "name": "Computer Science" }
     * @return The newly created department (as JSON) and a 201 Created status.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department createDepartment(@RequestBody CreateDepartmentRequest request) {
        return departmentService.createDepartment(request.getName());
    }
}