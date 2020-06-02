package com.telecominfraproject.wlan.portal.controller.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
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

    @RequestMapping(value = "/location/inSet", method = RequestMethod.GET)
    public ListOfLocations getAllInSet(@RequestParam Set<Long> locationIdSet) {
        LOG.debug("getAllInSet({})", locationIdSet);
        try {
            List<Location> result = locationServiceInterface.get(locationIdSet);
            LOG.debug("getAllInSet({}) return {} entries", locationIdSet, result.size());
            ListOfLocations ret = new ListOfLocations();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", locationIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/location/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Location> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Location> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Locations for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Location> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Locations for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Location> onePage = this.locationServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Locations for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }    
}
