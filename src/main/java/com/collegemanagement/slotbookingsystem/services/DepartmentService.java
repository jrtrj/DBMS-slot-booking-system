package com.collegemanagement.slotbookingsystem.services;

import com.collegemanagement.slotbookingsystem.repository.department.DepartmentDao;
import com.collegemanagement.slotbookingsystem.model.Department;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentDao departmentDao;

    public DepartmentService(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public List<Department> getAllDepartments() {
        return departmentDao.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentDao.findById(id);
    }

    public Department createDepartment(String departmentName) {
        Long newId = departmentDao.save(departmentName);
        
        return departmentDao.findById(newId).orElseThrow(() -> 
            new RuntimeException("Failed to create and retrieve department")
        );
    }
    
}