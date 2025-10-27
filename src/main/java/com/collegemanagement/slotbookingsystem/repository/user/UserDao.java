package com.collegemanagement.slotbookingsystem.repository.user;

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

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * READ: Fetches all users.
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * READ: Finds a single user by their unique ID.
     */
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * READ: Finds a single user by their email.
     * This is CRITICAL for our login logic.
     */
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * CREATE: Saves a new user to the database.
     * This method expects the password to ALREADY BE HASHED.
     * The hashing logic will live in the Service layer.
     * @return The auto-generated ID of the new user.
     */
    public Long save(User user) {
        String sql = "INSERT INTO users (email, password_hash, first_name, last_name, role, department_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.email());
            ps.setString(2, user.passwordHash());
            ps.setString(3, user.firstName());
            ps.setString(4, user.lastName());
            ps.setString(5, user.role().name()); // Convert Enum to string for DB
            ps.setLong(6, user.departmentId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
