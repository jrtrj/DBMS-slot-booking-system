package com.collegemanagement.slotbookingsystem.repository.department;

import com.collegemanagement.slotbookingsystem.model.Department;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentRowMapper implements RowMapper<Department> {

    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        // We use the constructor of the Department record to map the data
        return new Department(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("hod_id")
        );
    }
}