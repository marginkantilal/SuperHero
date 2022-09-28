package com.superhero.service;

import com.superhero.dao.SightingDao;
import com.superhero.model.Sighting;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class SightingService {
        
    private final SightingDao sightingDao;
    private final HeroService superService;
    private final LocationService locationService;
    
    public SightingService(SightingDao sightingDao, HeroService superService, LocationService locationService) {
        this.sightingDao = sightingDao;
        this.superService = superService;
        this.locationService= locationService;
    }
    
    
    public Sighting getSightingById(int id) {
        Sighting sighting = sightingDao.getSightingById(id);
        sighting.setSupper(superService.getSuperById(sighting.getSuperId())); //get super name
        sighting.setLocation(locationService.getLocationById(sighting.getLocationId())); //get location name
        
        return sighting;
    }
    
    public List<Sighting> getAllSightings() {
        List<Sighting> sightings = sightingDao.getAllSightings();
        sightings.forEach(sighting -> sighting.setSupper(superService.getSuperById(sighting.getSuperId()))); //get supers names
        sightings.forEach(sighting -> sighting.setLocation(locationService.getLocationById(sighting.getLocationId()))); //get locations names
        
        return sightings;
    }
    
    public Sighting addSighting(Sighting sighting) {
        Sighting newSighting = sightingDao.addSighting(sighting);
        return newSighting;
    }
    
    public void updateSighting(Sighting sighting) {
        sightingDao.updateSighting(sighting);
    }
    
    public void deleteSightingById(int id) {
        sightingDao.deleteSightingById(id);
    }
    
    public List<Sighting> getSightingsByDate(LocalDate date) {
        List<Sighting> sightings = sightingDao.getSightingsByDate(date);
        return sightings;
    }    
    
}
