package com.telecominfraproject.wlan.location.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.location.models.Location;

@Component
public class LocationServiceLocal implements LocationServiceInterface {

	@Autowired private LocationServiceController locationServiceController;
	
	@Override
	public List<Location> getAllForCustomer(int customerId) {
		return locationServiceController.getAllForCustomer(customerId);
	}

	@Override
	public Location get(long locationId) {
		return locationServiceController.get(locationId);			
	}

	@Override
	public Location create(Location location) {
		return locationServiceController.create(location);
	}

	@Override
	public Location update(Location location) {
		return locationServiceController.update(location);
	}

	@Override
	public Location delete(long locationId) {
		return locationServiceController.delete(locationId);
	}

	@Override
	public List<Location> getAllDescendants(long locationParentId) {
		return locationServiceController.getAllDescendants(locationParentId);
	}

	@Override
	public Location getTopLevelLocation(long locationId) {
		return locationServiceController.getTopLevelLocation(locationId);
	}

}
