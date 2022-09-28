package com.superhero.dao;

import com.superhero.dao.LocationDaoDB.LocationMapper;
import com.superhero.dao.OrganizationDaoDB.OrganizationMapper;
import com.superhero.dao.PowerDaoDB.PowerMapper;
import com.superhero.model.Location;
import com.superhero.model.Organisation;
import com.superhero.model.Power;
import com.superhero.model.Hero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HeroDaoDB implements HeroDao{
    
    @Autowired
    JdbcTemplate jdbc;
    OrganizationDaoDB orgDaoDb;
    LocationDaoDB locationDaoDb;
    PowerDaoDB powerDaodb;
    SightingDaoDB sightingDaoDb;

    
       @Override
    public Hero getSuperById(int id) {
        try {
            final String SELECT_SUPER_BY_ID = "SELECT * FROM super WHERE id = ?";
            Hero heroVillain = jdbc.queryForObject(SELECT_SUPER_BY_ID, new SuperMapper(), id);
            heroVillain.setLocations(getLocationsForSuper(id)); //set list of locations
            heroVillain.setPowers(getPowersForSuper(id)); //set list of powers
            heroVillain.setOrganizations(getOrganizationsForSuper(id)); //set list of organizations
            return heroVillain;
        } catch(DataAccessException ex) {
            return null;
        }
    }
   
      @Override
    public List<Location> getLocationsForSuper(int id) {       
        
        final String SELECT_LOCATIONS_FOR_SUPER = "SELECT l.* FROM location l "
                + "JOIN sighting si ON si.location_id = l.id WHERE si.super_id = ?";
        List<Location> locations = jdbc.query(SELECT_LOCATIONS_FOR_SUPER, new LocationMapper(), id);
        
        Set<Location> set = new HashSet<>(locations); //remove duplicate locations from list
        locations.clear();
        locations.addAll(set);

        return locations;
    } 

    @Override
    public List<Power> getPowersForSuper(int id) {
        final String SELECT_POWERS_FOR_SUPER = "SELECT p.* FROM power p "
                + "JOIN super_power sp ON sp.power_id = p.id WHERE sp.super_id = ?";
        return jdbc.query(SELECT_POWERS_FOR_SUPER, new PowerMapper(), id);    
    }
    
    @Override
    public List<Organisation> getOrganizationsForSuper(int id) {
        final String SELECT_ORGANIZATIONS_FOR_SUPER = "SELECT o.* FROM organization o "
                + "JOIN super_organization so ON so.organization_id = o.id WHERE so.super_id = ?";
        return jdbc.query(SELECT_ORGANIZATIONS_FOR_SUPER, new OrganizationMapper(), id); 
    }
   
    @Override
    public List<Hero> getAllSupers() {
        final String SELECT_ALL_SUPERS = "SELECT * FROM super";
        List<Hero> supers = jdbc.query(SELECT_ALL_SUPERS, new SuperMapper());
        associatePowerOrganizationSighting(supers);
        return supers;
    }
    
    private void associatePowerOrganizationSighting(List<Hero> supers ) {
        for (Hero heroVillain : supers) {
            heroVillain.setPowers(getPowersForSuper(heroVillain.getId()));
            heroVillain.setOrganizations(getOrganizationsForSuper(heroVillain.getId()));
            heroVillain.setLocations(getLocationsForSuper(heroVillain.getId()));
        }
    }

    @Override
    @Transactional
    public Hero addSuper(Hero heroVillain) {
        final String INSERT_SUPER = "INSERT INTO super(name, description) "
                + "VALUES(?,?)";
        
        jdbc.update(INSERT_SUPER,
                heroVillain.getName(),
                heroVillain.getDescription() );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        heroVillain.setId(newId);

        if(heroVillain.getPowers() != null && !heroVillain.getPowers().isEmpty()) {        
            insertPower(heroVillain);
        }
        if(heroVillain.getOrganizations() != null && !heroVillain.getOrganizations().isEmpty()) {
            insertOrganization(heroVillain);
        }
        return heroVillain;
    }  
    
    
    private void insertPower(Hero heroVillain) {
        final String INSERT_POWER = "INSERT INTO "
                + "super_power(super_id, power_id) VALUES(?,?)";
        for(Power power : heroVillain.getPowers()) {
            jdbc.update(INSERT_POWER, 
                    heroVillain.getId(),
                    power.getId());
        }
    }
    
    private void insertOrganization(Hero heroVillain) {
        final String INSERT_ORGANIZATION = "INSERT INTO "
                + "super_organization(super_id, organization_id) VALUES(?,?)";
        for(Organisation organization : heroVillain.getOrganizations()) {
            jdbc.update(INSERT_ORGANIZATION, 
                    heroVillain.getId(),
                    organization.getId());
        }
    }
    
    
      @Override
    @Transactional
    public void updateSuper(Hero heroVillain) {
        final String UPDATE_SUPER = "UPDATE super SET name = ?, description = ? WHERE id = ?";
        jdbc.update(UPDATE_SUPER, 
                heroVillain.getName(), 
                heroVillain.getDescription(), 
                heroVillain.getId());
        
        final String DELETE_SUPER_POWER = "DELETE FROM super_power WHERE super_id = ?";
        jdbc.update(DELETE_SUPER_POWER, heroVillain.getId());
        insertPower(heroVillain);
        
        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE super_id = ?";
        jdbc.update(DELETE_SUPER_ORGANIZATION, heroVillain.getId());        
        insertOrganization(heroVillain);
    }

    
     @Override
    @Transactional
    public void deleteSuperById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE super_id = ?";
        jdbc.update(DELETE_SIGHTING, id);
        
        final String DELETE_SUPER_POWER = "DELETE FROM super_power WHERE super_id = ?";
        jdbc.update(DELETE_SUPER_POWER, id);
        
        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE super_id = ?";
        jdbc.update(DELETE_SUPER_ORGANIZATION, id);
        
        final String DELETE_SUPER = "DELETE FROM super WHERE id = ?";
        jdbc.update(DELETE_SUPER, id);
    }

    
      @Override
    public List<Hero> getSupersByLocation(Location location) {
        final String SELECT_SUPERS_FOR_LOCATION = "SELECT s.* FROM super s JOIN "
                + "sighting sig ON sig.super_id = s.id WHERE sig.location_id = ?";
        List<Hero> supers = jdbc.query(SELECT_SUPERS_FOR_LOCATION, 
                new SuperMapper(), location.getId());
        
        Set<Hero> set = new HashSet<>(supers); //remove duplicate supers from list
        supers.clear();
        supers.addAll(set);

        associatePowerOrganizationSighting(supers);
        return supers;
    }

    
     @Override
    public List<Hero> getSupersByOrganization(Organisation organization) {
        final String SELECT_SUPERS_FOR_LOCATION = "SELECT s.* FROM super s JOIN "
                + "super_organization so ON so.super_id = s.id WHERE so.organization_id = ?";
        List<Hero> supers = jdbc.query(SELECT_SUPERS_FOR_LOCATION, 
                new SuperMapper(), organization.getId());
        associatePowerOrganizationSighting(supers);
        return supers;
    }

    
    @Override
    public void removePowerForSuper(int superId, int powerId) {
        final String DELETE_POWER_FOR_SUPER = "DELETE FROM super_power WHERE super_id = ? AND power_id = ?";
        jdbc.update(DELETE_POWER_FOR_SUPER, superId, powerId);
    }
    
    @Override
    public void removeOrganizationForSuper(int superId, int organizationId) {
        final String DELETE_ORGANIZATION_FOR_SUPER = "DELETE FROM super_organization WHERE super_id = ? AND organization_id = ?";
        jdbc.update(DELETE_ORGANIZATION_FOR_SUPER, superId, organizationId);
    }
    
    
    public static final class SuperMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int index) throws SQLException {
            Hero heroVillain = new Hero();
            heroVillain.setId(rs.getInt("id"));
            heroVillain.setName(rs.getString("name"));
            heroVillain.setDescription(rs.getString("description"));         
            return heroVillain;
        }
    }
    
}


