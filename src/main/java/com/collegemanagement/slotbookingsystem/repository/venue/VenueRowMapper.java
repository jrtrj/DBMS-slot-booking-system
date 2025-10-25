package com.collegemanagement.slotbookingsystem.repository.venue;

import com.collegemanagement.slotbookingsystem.model.Venue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class VenueRowMapper implements RowMapper<Venue> {

	@Override
	public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Venue(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getString("block"),
			rs.getInt("capacity"),
			rs.getLong("department_id")
				);
	}
	
}