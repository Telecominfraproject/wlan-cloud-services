package com.telecominfraproject.wlan.portal.controller.location;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class LocationPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(LocationPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfLocations extends ArrayList<Location> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private LocationServiceInterface locationServiceInterface;

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public Location getLocation(@RequestParam long locationId) {
        LOG.debug("Getting location {}", locationId);

        Location location = locationServiceInterface.get(locationId);

        return location;
    }

    @RequestMapping(value = "/location", method = RequestMethod.PUT)
    public Location updateLocation(@RequestBody Location location) {
        LOG.debug("Updating location {}", location.getId());

        Location ret = locationServiceInterface.update(location);

        return ret;
    }

    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public Location createLocation(@RequestBody Location location) {
        LOG.debug("Creating location {}", location.getId());

        Location ret = locationServiceInterface.create(location);

        return ret;
    }

    @RequestMapping(value = "/location", method = RequestMethod.DELETE)
    public Location deleteLocation(@RequestParam long locationId) {
        LOG.debug("Deleting location {}", locationId);

        Location ret = locationServiceInterface.delete(locationId);

        return ret;
    }
    
    @RequestMapping(value="/location/top", method = RequestMethod.GET)
    public Location getTopLevelLocation(@RequestParam Long locationId) {
    	Location location = locationServiceInterface.getTopLevelLocation(locationId);
        return location;
    }


    @RequestMapping(value = "/location/allForCustomer", method = RequestMethod.GET)
    public ListOfLocations getAllForCustomer(@RequestParam Integer customerId) {
    	LOG.debug("getAllForCustomer({})", customerId);
        List<Location> result = locationServiceInterface.getAllForCustomer(customerId);
        LOG.debug("getAllForCustomer({}) return {} entries", customerId, result.size());
        ListOfLocations ret = new ListOfLocations();
        ret.addAll(result);
        return ret;
    }
    
    @RequestMapping(value = "/location/allDescendants", method = RequestMethod.GET)
    public ListOfLocations getAllDescendants(@RequestParam Long locationId) {
    	LOG.debug("getAllDescendants({})", locationId);
        List<Location> result = locationServiceInterface.getAllDescendants(locationId);
        LOG.debug("getAllDescendants({}) return {} entries", locationId, result.size());
        ListOfLocations ret = new ListOfLocations();
        ret.addAll(result);
        return ret;
    }

}
