package com.telecominfraproject.wlan.firmware.controller;

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

import com.telecominfraproject.wlan.firmware.datastore.FirmwareDatastore;
import com.telecominfraproject.wlan.firmware.models.Firmware;
import com.telecominfraproject.wlan.firmware.models.events.FirmwareAddedEvent;
import com.telecominfraproject.wlan.firmware.models.events.FirmwareChangedEvent;
import com.telecominfraproject.wlan.firmware.models.events.FirmwareRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/firmware")
public class FirmwareController {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareController.class);

    public static class ListOfFirmwares extends ArrayList<Firmware> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private FirmwareDatastore firmwareDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Firmware.
     *  
     * @param Firmware
     * @return stored Firmware object
     * @throws RuntimeException if Firmware record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Firmware create(@RequestBody Firmware firmware ) {

        LOG.debug("Creating Firmware {}", firmware);

        if (BaseJsonModel.hasUnsupportedValue(firmware)) {
            LOG.error("Failed to create Firmware, request contains unsupported value: {}", firmware);
            throw new DsDataValidationException("Firmware contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (firmware.getCreatedTimestamp() == 0) {
        	firmware.setCreatedTimestamp(ts);
        }
        firmware.setLastModifiedTimestamp(ts);

        Firmware ret = firmwareDatastore.create(firmware);

        LOG.debug("Created Firmware {}", ret);

        FirmwareAddedEvent event = new FirmwareAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Firmware by id
     * @param firmwareId
     * @return Firmware for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Firmware get(@RequestParam long firmwareId ) {
        
        LOG.debug("Retrieving Firmware {}", firmwareId);
        
        Firmware ret = firmwareDatastore.get(firmwareId);

        LOG.debug("Retrieved Firmware {}", ret);

        return ret;
    }

    /**
     * Retrieves Firmware by id
     * @param firmwareId
     * @return Firmware for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Firmware getOrNull(@RequestParam long firmwareId ) {
        
        LOG.debug("Retrieving Firmware {}", firmwareId);
        
        Firmware ret = firmwareDatastore.getOrNull(firmwareId);

        LOG.debug("Retrieved Firmware {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfFirmwares getAllInSet(@RequestParam Set<Long> firmwareIdSet) {
        LOG.debug("getAllInSet({})", firmwareIdSet);
        try {
            List<Firmware> result = firmwareDatastore.get(firmwareIdSet);
            LOG.debug("getAllInSet({}) return {} entries", firmwareIdSet, result.size());
            ListOfFirmwares ret = new ListOfFirmwares();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", firmwareIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Firmware> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Firmware> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Firmwares for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Firmware> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Firmwares for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Firmware> onePage = this.firmwareDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Firmwares for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Firmware record
     * 
     * @param Firmware
     * @return updated Firmware object
     * @throws RuntimeException if Firmware record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Firmware update(@RequestBody Firmware firmware){
        
        LOG.debug("Updating Firmware {}", firmware);
        
        if (BaseJsonModel.hasUnsupportedValue(firmware)) {
            LOG.error("Failed to update Firmware, request contains unsupported value: {}", firmware);
            throw new DsDataValidationException("Firmware contains unsupported value");
        }

        Firmware ret = firmwareDatastore.update(firmware);

        LOG.debug("Updated Firmware {}", ret);

        FirmwareChangedEvent event = new FirmwareChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Firmware record
     * 
     * @param firmwareId
     * @return deleted Firmware object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Firmware delete(@RequestParam long firmwareId ) {
        
        LOG.debug("Deleting Firmware {}", firmwareId);
        
        Firmware ret = firmwareDatastore.delete(firmwareId);

        LOG.debug("Deleted Firmware {}", ret);
        
        FirmwareRemovedEvent event = new FirmwareRemovedEvent(ret);
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
