package com.collegemanagement.slotbookingsystem.repository.Department;

import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.collegemanagement.slotbookingsystem.model.Department;

import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


@Repository
public class DepartmentDao {
    
    private final JdbcTemplate jdbctemplate;
    public DepartmentDao(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    public List<Department> findAll() {
        final String sql = "SELECT id, name, hod_id FROM departments";
        return jdbctemplate.query(sql,new DepartmentRowMapper());
    }

    public Optional<Department> findById(Long id) {
       String sql = "SELECT id,name,hod_id FROM departments WHERE id = ?";
       try{
            //queryForObject returns a single object
            Department department = jdbctemplate.queryForObject(sql, new DepartmentRowMapper(), id);
            return Optional.ofNullable(department);
       }
       catch(EmptyResultDataAccessException e) {
           return Optional.empty();
            
       }
    }

    public Optional<Department> findByHodId(Long hod_id) {
        String sql = "SELECT id,name,hod_id FROM departments WHERE hod_id=?";
        try{
            Department department = jdbctemplate.queryForObject(sql,new DepartmentRowMapper(), hod_id);
            return Optional.ofNullable(department);
        }
        catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(String departmentName) {
        String sql = "INSERT INTO departments (name) VALUES (?)";
        //KeyHolder retrieves Auto-generated keys
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //AHH functional things
		jdbctemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, departmentName);
            return ps;
        }, keyHolder);
		
		return keyHolder.getKey().longValue();
    }
    
    public int update(Department department) {
        String sql = "UPDATE departments SET name = ?, hod_id = ? WHERE id = ?";
        return jdbctemplate.update(
                sql,
                department.name(),
                department.hodId(),
                department.id()
        );
    }
    
    public int deleteById(Long id) {
        String sql = "DELETE FROM departments WHERE id = ?";
        return jdbctemplate.update(sql, id);
    }

}
