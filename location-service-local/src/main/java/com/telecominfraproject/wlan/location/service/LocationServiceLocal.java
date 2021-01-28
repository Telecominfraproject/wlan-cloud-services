package com.telecominfraproject.wlan.location.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.location.models.Location;

@Component
public class LocationServiceLocal implements LocationServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(LocationServiceLocal.class);

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
	
	@Override
    public List<Location> getAllAncestors(long locationParentId) {
        return locationServiceController.getAllAncestors(locationParentId);
    }

    @Override
    public List<Location> get(Set<Long> locationIdSet) {
        LOG.debug("calling locationController.getAllInSet {} ", locationIdSet);
        return locationServiceController.getAllInSet(locationIdSet);
    }
    
    @Override
    public PaginationResponse<Location> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Location> context) {
        LOG.debug("calling locationController.getForCustomer {} ", customerId);
        return locationServiceController.getForCustomer(customerId, sortBy, context);
    }


}
