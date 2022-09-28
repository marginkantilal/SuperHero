package com.superhero.service;

import com.superhero.dao.LocationDao;
import com.superhero.model.Location;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
        
    private final LocationDao locationDao;
    
    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }
    
    
    public Location getLocationById(int id) {
        Location location = locationDao.getLocationById(id);
        return location;
    }
    
    public List<Location> getAllLocations() {
        List<Location> locations = locationDao.getAllLocations();
        return locations;
    }
    
    public Location addLocation(Location location) {
        Location newLocation = locationDao.addLocation(location);
        return newLocation;
    }
    
    public void updateLocation(Location location) {
        locationDao.updateLocation(location);
    }
    
    public void deleteLocationById(int id) {
        locationDao.deleteLocationById(id);
    }
    
}
