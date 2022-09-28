package com.superhero.service;

import com.superhero.dao.HeroDao;
import com.superhero.model.Location;
import com.superhero.model.Organisation;
import com.superhero.model.Power;
import com.superhero.model.Hero;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class HeroService {
    
    private final HeroDao superDao;
    
    private final PowerService powerService;
    private final OrganizationService organizationService;
    
    
    public HeroService(HeroDao superDao, PowerService powerService, OrganizationService organizationService){
        this.powerService = powerService;
        this.organizationService = organizationService;
        this.superDao = superDao;
    }
    
    
    public Hero getSuperById(int id) {
        Hero supper = superDao.getSuperById(id);
        return supper;
    }
    
    public List<Hero> getAllSupers() {
        List<Hero> supers = superDao.getAllSupers();
        return supers;
    }    
    
    public Hero addSuper(Hero heroVillain, List<String> powerIds, List<String> organizationIds) {
        List<Power> powers = new ArrayList<>();
        for(String powerId : powerIds) {
            powers.add(powerService.getPowerById(Integer.parseInt(powerId)));
        }
        
        List<Organisation> organizations = new ArrayList<>();
        for(String organizationId : organizationIds) {
           organizations.add(organizationService.getOrganizationById(Integer.parseInt(organizationId)));
        }
        
        heroVillain.setPowers(powers);
        heroVillain.setOrganizations(organizations);
        
        Hero supper = superDao.addSuper(heroVillain);
        return supper;
    }
    
    public void updateSuper(Hero heroVillain, List<String> powerIds, List<String> organizationIds) {
        List<Power> powers = new ArrayList<>();
        for(String powerId : powerIds) {
            powers.add(powerService.getPowerById(Integer.parseInt(powerId)));
        }
        
        List<Organisation> organizations = new ArrayList<>();
        for(String organizationId : organizationIds) {
           organizations.add(organizationService.getOrganizationById(Integer.parseInt(organizationId)));
        }
        
        heroVillain.setPowers(powers);
        heroVillain.setOrganizations(organizations);
        
        superDao.updateSuper(heroVillain);    
    }
    
    public void deleteSuperById(int id) {
        superDao.deleteSuperById(id);
    }
    
    public List<Hero> getSupersByLocation(Location location) {
        List<Hero> supers = superDao.getSupersByLocation(location);
        return supers;
    }
    
    public List<Hero> getSupersByOrganization(Organisation organization) {
        List<Hero> supers = superDao.getSupersByOrganization(organization);
        return supers;
    }
    
    public List<Location> getLocationsForSuper(int id) {
        List<Location> locations = superDao.getLocationsForSuper(id);
        return locations;
    }
    
    public List<Power> getPowersForSuper(int id) {
        List<Power> powers = superDao.getPowersForSuper(id);
        return powers;
    }
    
    public List<Organisation> getOrganizationsForSuper(int id) {
        List<Organisation> organizations = superDao.getOrganizationsForSuper(id);
        return organizations;
    }
    
    public void removePowerForSuper(int superId, int powerId) {
        superDao.removePowerForSuper(superId, powerId);
    }
    
    public void removeOrganizationForSuper(int superId, int organizationId) {
        superDao.removeOrganizationForSuper(superId, organizationId);
    } 
    
}
