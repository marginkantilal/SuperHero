package com.superhero.dao;

import java.util.List;

import com.superhero.model.Organisation;

public interface OrganizationDao {
    Organisation getOrganizationById(int id);
    List<Organisation> getAllOrganizations();
    Organisation addOrganization(Organisation organization);
    void updateOrganization(Organisation organization);
    void deleteOrganizationById(int id);    
}