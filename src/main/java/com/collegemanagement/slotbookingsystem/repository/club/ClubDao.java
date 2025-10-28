package com.collegemanagement.slotbookingsystem.repository.club;

import com.collegemanagement.slotbookingsystem.model.Club;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ClubDao {

    private final JdbcTemplate jdbcTemplate;

    public ClubDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Club> findAll() {
        String sql = "SELECT id, name, faculty_advisor_id FROM clubs";
        return jdbcTemplate.query(sql, new ClubRowMapper());
    }

    public Optional<Club> findById(Long id) {
        String sql = "SELECT id, name, faculty_advisor_id FROM clubs WHERE id = ?";
        try {
            Club club = jdbcTemplate.queryForObject(sql, new ClubRowMapper(), id);
            return Optional.ofNullable(club);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(String clubName) {
        String sql = "INSERT INTO clubs (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clubName);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    
}