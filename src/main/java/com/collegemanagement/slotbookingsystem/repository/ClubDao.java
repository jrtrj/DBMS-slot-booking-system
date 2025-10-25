package com.collegemanagement.slotbookingsystem.repository;

import com.collegemanagement.slotbookingsystem.model.Club;
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
public class ClubDao {

    private final JdbcTemplate jdbcTemplate;

    public ClubDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * READ: Fetches all clubs from the database.
     */
    public List<Club> findAll() {
        String sql = "SELECT id, name, faculty_advisor_id FROM clubs";
        return jdbcTemplate.query(sql, new ClubRowMapper());
    }

    /**
     * READ: Finds a single club by its unique ID.
     */
    public Optional<Club> findById(Long id) {
        String sql = "SELECT id, name, faculty_advisor_id FROM clubs WHERE id = ?";
        try {
            Club club = jdbcTemplate.queryForObject(sql, new ClubRowMapper(), id);
            return Optional.ofNullable(club);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * CREATE: Saves a new club to the database.
     * @return The auto-generated ID of the new club.
     */
    public Long save(String clubName) {
        String sql = "INSERT INTO clubs (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clubName);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
    
    // We will add `update` and `delete` methods later.
}