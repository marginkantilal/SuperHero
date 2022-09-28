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

import com.superhero.model.Organisation;

@Repository
public class OrganizationDaoDB implements OrganizationDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organisation getOrganizationById(int id) {
        try {
            final String SELECT_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE id = ?";
            return jdbc.queryForObject(SELECT_ORGANIZATION_BY_ID, new OrganizationDaoDB.OrganizationMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    
      @Override
    public List<Organisation> getAllOrganizations() {
        final String SELECT_ALL_ORGANIZATIONS = "SELECT * FROM organization";
        return jdbc.query(SELECT_ALL_ORGANIZATIONS, new OrganizationDaoDB.OrganizationMapper());
    }

    
    /** ********** addOrganization. ********** **/
    //This method is @Transactional so we can retrieve the new ID.
    @Override
    @Transactional
    public Organisation addOrganization(Organisation organization) {
       final String INSERT_ORGANIZATION = "INSERT INTO organization(name, description, address, contact) VALUES(?,?,?,?)";
        jdbc.update(INSERT_ORGANIZATION,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        return organization;
    }
    
   @Override
    @Transactional
    public void updateOrganization(Organisation organization) {        
        final String UPDATE_ORGANIZATION = "UPDATE organization SET name = ?, description = ?, "
                + "address = ?, contact = ? WHERE id = ?";
        jdbc.update(UPDATE_ORGANIZATION, 
                organization.getName(), 
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact(),
                organization.getId());
    }

    

    @Override
    @Transactional
    public void deleteOrganizationById(int id) {
        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE organization_id = ?";
        jdbc.update(DELETE_SUPER_ORGANIZATION, id);
        
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE id = ?";
        jdbc.update(DELETE_ORGANIZATION, id);
    }
    
    
     public static final class OrganizationMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation organization = new Organisation();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));
            organization.setContact(rs.getString("contact"));

            
            return organization;
        }
    }
    
}
