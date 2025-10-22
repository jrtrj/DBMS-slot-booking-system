import java.util.list;
import java.util.optional;

import org.springframework.stereotype.repository;
import org.springframework.dao.emptyresultdataaccessexception;
import org.springframework.jdbc.core.jdbctemplate;

package com.collegemanagement.slotbookingsystem.dao.Department;

@Repository
public class DepartmentDao {
    
    private final JdbcTemplate jdbctemplate;
    public DepartmentDao(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    public List<Department> findAll() {
        final String sql = "SELECT id, name, hod_id FROM department";
        return jdbctemplate.query(sql,new DepartmentRowMapper());
    }

    public Optional<Department> findById(Long id) {
       String sql = "SELECT id,name,hod_id FROM department WHERE id = ?";
       try{
            Department department = jdbcTemplate.queryForObject(sql, new DepartmentRowMapper());
            return Optional.ofNullable(department);
       }
       catch(EmptyResultDataAccessException e) {
           return Optional.empty();
            
       }
    }

    public Optional<Department> findByHodId(Lond hod_id) {
        String sql = "SELECT id,name,hod_id FROM department WHERE hod_id=?"
        try{
            Department department = jdbcTemplate.queryForObject(sql,new DepartmentRowMapper());
            return Optional.ofNullable(department);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(string departmentName) {
        String sql = "INSERT INTO (name) VALUES ?";
    }

}
