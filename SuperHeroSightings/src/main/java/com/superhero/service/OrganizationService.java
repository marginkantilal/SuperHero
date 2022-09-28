package com.superhero.service;

import com.superhero.dao.OrganizationDao;
import com.superhero.model.Organisation;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
        
    private final OrganizationDao organizationDao;
    
    public OrganizationService(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }
    
    
    public Organisation getOrganizationById(int id) {
        Organisation organization = organizationDao.getOrganizationById(id);
        return organization;
    }
    
    public List<Organisation> getAllOrganizations(){
        List<Organisation> organizations = organizationDao.getAllOrganizations();
        return organizations;
    }
    
    public Organisation addOrganization(Organisation organization) {
        Organisation newOrganization = organizationDao.addOrganization(organization);
        return newOrganization;
    }
    
    public void updateOrganization(Organisation organization) {
        organizationDao.updateOrganization(organization);
    }
    
    public void deleteOrganizationById(int id) {
        organizationDao.deleteOrganizationById(id);
    }
      
}
