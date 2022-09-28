package com.superhero.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superhero.model.Power;

@Repository
public class PowerDaoDB implements PowerDao{
    
    @Autowired
    JdbcTemplate jdbc;

    
    /** ********** getPowerowerById. ********** **/
    //Create the SELECT query string and use it in queryForObject to get the one Power we are searching for.
    //Surround the code with a try-catch that will catch the exception thrown when there is no Power with that ID, so we can return null in that situation.
    @Override
    public Power getPowerById(int id) {
        try {
            final String SELECT_POWER_BY_ID = "SELECT * FROM power WHERE id = ?";
            return jdbc.queryForObject(SELECT_POWER_BY_ID, new PowerDaoDB.PowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    
    /** ********** getAllPowers. ********** **/
    //We simply create our SELECT query and use it in the query method to return a list of all Powers.
    //If no Powers are found, it will return an empty list.
    @Override
    public List<Power> getAllPowers() {
        final String SELECT_ALL_POWERS = "SELECT * FROM power";
        return jdbc.query(SELECT_ALL_POWERS, new PowerMapper());
    }


    /** ********** addPower. ********** **/
    //Our method is @Transactional because we are using the LAST_INSERT_ID query later.
    //We create our INSERT query and use it with the update method and the Power data in order.
    //We then get the ID for our new Power using the LAST_INSERT_ID MySQL function and set it in the Power before returning it.    
    @Override
    @Transactional
    public Power addPower(Power power) {
        final String INSERT_POWER = "INSERT INTO power(name, description) "
                + "VALUES(?,?)";
        jdbc.update(INSERT_POWER,
                power.getName(),
                power.getDescription());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        power.setId(newId);
        return power;
    }

    
    /** ********** updatePower. ********** **/
    //We create the UPDATE query and use it in the update method with the appropriate Power data.
    @Override
    public void updatePower(Power power) {
        final String UPDATE_POWER = "UPDATE power SET name = ?, description = ? "
                + "WHERE id = ?";
        jdbc.update(UPDATE_POWER, 
                power.getName(), 
                power.getDescription(),
                power.getId());   
    }
    

    /** ********** deletePowerById. ********** **/
    //We'll make the method @Transactional because we are running multiple queries that modify the database in this method.
    //We start by deleting the super_power entries associated with the Power.
    //We end by deleting the Power itself.
    //Order matters here because we can't delete something that is being referenced by another table.
    @Override
    @Transactional
    public void deletePowerById(int id) {        
        final String DELETE_SUPER_POWER = "DELETE FROM super_power WHERE power_id = ?";
        jdbc.update(DELETE_SUPER_POWER, id);
        
        final String DELETE_POWER = "DELETE FROM power WHERE id = ?";
        jdbc.update(DELETE_POWER, id);
    }
    
    
    /** ********** Get data from database with Mapper function. ********** **/
     public static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setId(rs.getInt("id"));
            power.setName(rs.getString("name"));
            power.setDescription(rs.getString("description"));
            return power;
        }
    }
    
}
