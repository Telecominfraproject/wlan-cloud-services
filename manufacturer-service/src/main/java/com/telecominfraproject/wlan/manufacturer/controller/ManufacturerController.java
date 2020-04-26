package com.telecominfraproject.wlan.manufacturer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastore;
import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;
import com.telecominfraproject.wlan.manufacturer.models.events.ManufacturerAddedEvent;
import com.telecominfraproject.wlan.manufacturer.models.events.ManufacturerChangedEvent;
import com.telecominfraproject.wlan.manufacturer.models.events.ManufacturerRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/manufacturer")
public class ManufacturerController {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerController.class);

    public static class ListOfManufacturers extends ArrayList<Manufacturer> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private ManufacturerDatastore manufacturerDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Manufacturer.
     *  
     * @param Manufacturer
     * @return stored Manufacturer object
     * @throws RuntimeException if Manufacturer record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Manufacturer create(@RequestBody Manufacturer manufacturer ) {

        LOG.debug("Creating Manufacturer {}", manufacturer);

        if (BaseJsonModel.hasUnsupportedValue(manufacturer)) {
            LOG.error("Failed to create Manufacturer, request contains unsupported value: {}", manufacturer);
            throw new DsDataValidationException("Manufacturer contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (manufacturer.getCreatedTimestamp() == 0) {
        	manufacturer.setCreatedTimestamp(ts);
        }
        manufacturer.setLastModifiedTimestamp(ts);

        Manufacturer ret = manufacturerDatastore.create(manufacturer);

        LOG.debug("Created Manufacturer {}", ret);

        ManufacturerAddedEvent event = new ManufacturerAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Manufacturer by id
     * @param manufacturerId
     * @return Manufacturer for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Manufacturer get(@RequestParam long manufacturerId ) {
        
        LOG.debug("Retrieving Manufacturer {}", manufacturerId);
        
        Manufacturer ret = manufacturerDatastore.get(manufacturerId);

        LOG.debug("Retrieved Manufacturer {}", ret);

        return ret;
    }

    /**
     * Retrieves Manufacturer by id
     * @param manufacturerId
     * @return Manufacturer for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Manufacturer getOrNull(@RequestParam long manufacturerId ) {
        
        LOG.debug("Retrieving Manufacturer {}", manufacturerId);
        
        Manufacturer ret = manufacturerDatastore.getOrNull(manufacturerId);

        LOG.debug("Retrieved Manufacturer {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfManufacturers getAllInSet(@RequestParam Set<Long> manufacturerIdSet) {
        LOG.debug("getAllInSet({})", manufacturerIdSet);
        try {
            List<Manufacturer> result = manufacturerDatastore.get(manufacturerIdSet);
            LOG.debug("getAllInSet({}) return {} entries", manufacturerIdSet, result.size());
            ListOfManufacturers ret = new ListOfManufacturers();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", manufacturerIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Manufacturer> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<Manufacturer> paginationContext) {

        LOG.debug("Looking up Manufacturers for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Manufacturer> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Manufacturers for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Manufacturer> onePage = this.manufacturerDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Manufacturers for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Manufacturer record
     * 
     * @param Manufacturer
     * @return updated Manufacturer object
     * @throws RuntimeException if Manufacturer record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Manufacturer update(@RequestBody Manufacturer manufacturer){
        
        LOG.debug("Updating Manufacturer {}", manufacturer);
        
        if (BaseJsonModel.hasUnsupportedValue(manufacturer)) {
            LOG.error("Failed to update Manufacturer, request contains unsupported value: {}", manufacturer);
            throw new DsDataValidationException("Manufacturer contains unsupported value");
        }

        Manufacturer ret = manufacturerDatastore.update(manufacturer);

        LOG.debug("Updated Manufacturer {}", ret);

        ManufacturerChangedEvent event = new ManufacturerChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Manufacturer record
     * 
     * @param manufacturerId
     * @return deleted Manufacturer object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Manufacturer delete(@RequestParam long manufacturerId ) {
        
        LOG.debug("Deleting Manufacturer {}", manufacturerId);
        
        Manufacturer ret = manufacturerDatastore.delete(manufacturerId);

        LOG.debug("Deleted Manufacturer {}", ret);
        
        ManufacturerRemovedEvent event = new ManufacturerRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

    
}
