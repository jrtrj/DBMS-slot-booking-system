package com.collegemanagement.slotbookingsystem.repository.user;

import com.collegemanagement.slotbookingsystem.model.Role;
import com.collegemanagement.slotbookingsystem.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    //JdbcTemplate is stateless and thread-safe
    //Created initially by HikariCP
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        // Use query which returns a list to avoid throwing an exception for an empty result.
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.stream().findFirst();
    }

    // for login logic
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        // This is a cleaner way to handle cases where no user is found.
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email);
        return users.stream().findFirst();
    }

    /**
     * Finds the first user matching a specific role.
     * Assumes some roles (like PRINCIPAL) are unique.
     */
    public Optional<User> findByRole(Role role) {
        String sql = "SELECT * FROM users WHERE role = ? LIMIT 1";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), role.name());
        return users.stream().findFirst();
    }

     // This method expects the password to ALREADY BE HASHED.
     // The hashing logic will live in the Service layer.
    public Long save(User user) {
        String sql = "INSERT INTO users (email, password_hash, first_name, last_name, role, department_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.email());
            ps.setString(2, user.hashedPassword());
            ps.setString(3, user.firstName());
            ps.setString(4, user.lastName());
            ps.setString(5, user.role().name());
            ps.setLong(6, user.departmentId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
