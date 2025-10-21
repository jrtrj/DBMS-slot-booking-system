package com.collegemanagement.slotbookingsystem.dao;

import com.collegemanagement.slotbookingsystem.model.Venue;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row from the 'venues' table in a ResultSet to a Venue record.
 * This class translates raw SQL data into our immutable Java object.
 */
public class VenueRowMapper implements RowMapper<Venue> {

    @Override
    public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
        // We extract each column by its name from the result set...
        return new Venue(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("block"),
                rs.getInt("capacity"),
                rs.getLong("department_id")
        );
        // ...and use them to construct a new Venue record in a single step.
    }
}
