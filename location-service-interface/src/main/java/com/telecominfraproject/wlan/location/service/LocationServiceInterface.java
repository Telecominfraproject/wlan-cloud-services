package com.telecominfraproject.wlan.location.service;

import java.util.List;

import com.telecominfraproject.wlan.location.models.Location;

public interface LocationServiceInterface {

    List<Location> getAllForCustomer(int customerId);
    Location get(long locationId);
    Location create(Location location);
    Location update(Location location);
    Location delete(long locationId);

    /**
     * Recursively return all the children of the specified location id.
     * ie: Grandpa -> Homer -> Bart. If you specify Grandpa, you'll receive both Homer and Bart.
     *     (but you will NOT receive Grandpa)
     */
    List<Location> getAllDescendants(long locationParentId);


    /**
     * @return the top-level location for a specified  location
     */
    Location getTopLevelLocation(long locationId);
}
