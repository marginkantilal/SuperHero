package com.superhero.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superhero.model.Sighting;

@Repository
public class SightingDaoDB implements SightingDao{
    
    @Autowired
    JdbcTemplate jdbc;
    

    @Override
    public Sighting getSightingById(int id) {
        try {
            final String SELECT_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE id = ?";
            return jdbc.queryForObject(SELECT_SIGHTING_BY_ID, new SightingDaoDB.SightingMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Sighting> getAllSightings() {        
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting";
        return jdbc.query(SELECT_ALL_SIGHTINGS, new SightingDaoDB.SightingMapper());
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(super_id, location_id, date) "
                + "VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getSuperId(),
                sighting.getLocationId(),
                sighting.getDate());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }    
    

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET super_id = ?, location_id = ?, date = ? "
                + "WHERE id = ?";
        jdbc.update(UPDATE_SIGHTING, 
                sighting.getSuperId(), 
                sighting.getLocationId(),
                sighting.getDate(),
                sighting.getId());  
    }

    @Override
    public void deleteSightingById(int id) {        
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {          
        final String SELECT_SIGHTINGS_BY_DATE = "SELECT * FROM sighting WHERE date = ?";
        return jdbc.query(SELECT_SIGHTINGS_BY_DATE, new SightingDaoDB.SightingMapper());
    }
    
    
    /** ********** Get data from database with Mapper function. ********** **/
     public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("id"));
            sighting.setSuperId(rs.getInt("super_id"));
            sighting.setLocationId(rs.getInt("location_id"));
            sighting.setDate(rs.getDate("date").toLocalDate());
            return sighting;
        }
    }
    
}
