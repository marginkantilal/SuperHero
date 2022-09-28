package com.superhero.dao;

import java.util.List;

import com.superhero.model.Location;
import com.superhero.model.Organisation;
import com.superhero.model.Power;
import com.superhero.model.Hero;

public interface HeroDao {
    Hero getSuperById(int id);
    List<Hero> getAllSupers();
    Hero addSuper(Hero heroVillain);
    void updateSuper(Hero heroVillain);
    void deleteSuperById(int id);
    
    List<Hero> getSupersByLocation(Location location);
    List<Hero> getSupersByOrganization(Organisation organization);
    
    List<Location> getLocationsForSuper(int id);
    List<Power> getPowersForSuper(int id);
    List<Organisation> getOrganizationsForSuper(int id);
    
    void removePowerForSuper(int superId, int powerId);
    void removeOrganizationForSuper(int superId, int organizationId);
}