package com.collegemanagement.slotbookingsystem.repository.user;

import com.collegemanagement.slotbookingsystem.model.Role;
import com.collegemanagement.slotbookingsystem.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Handle nullable department_id correctly
        Long departmentId = rs.getObject("department_id", Long.class);

        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                Role.valueOf(rs.getString("role")),
                departmentId // Use the nullable Long
        );
    }
}
