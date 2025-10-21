package com.collegemanagement.slotbookingsystem.dao.venue;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.collegemanagement.slotbookingsystem.model.Venue;

@Repository
public class VenueDao {

	private final JdbcTemplate jdbcTemplate;
	//@Autowired
	public VenueDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Venue> findAll() {
		final String sql = "SELECT id, name, block, capacity, department_id FROM venues";
		// query() for multiple rows and translator
		return jdbcTemplate.query(sql, new VenueRowMapper());
	}
	
	public Optional<Venue> findById(Long id) {
		try {
			final String sql = "SELECT id, name, block, capacity, department_id FROM venues where id = ?";
			Venue venue = jdbcTemplate.queryForObject(sql, new VenueRowMapper(), id);
			return Optional.ofNullable(venue);
		}	
		catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	public int save(Venue venue) {
	String sql = "INSERT INTO venues (name, block, capacity, department_id) VALUES (?, ?, ?, ?)";
	return jdbcTemplate.update(
			sql,
			venue.name(),
			venue.block(),
			venue.capacity(),
			venue.departmentId()
			);
	}
}
