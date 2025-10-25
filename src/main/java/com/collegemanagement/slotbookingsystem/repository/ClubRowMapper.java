package com.collegemanagement.slotbookingsystem.repository;

import com.collegemanagement.slotbookingsystem.model.Club;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row from the 'clubs' table in a ResultSet to a Club record.
 */
public class ClubRowMapper implements RowMapper<Club> {

    @Override
    public Club mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Club(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("faculty_advisor_id")
        );
    }
}