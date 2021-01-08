package com.telecominfraproject.wlan.systemevent.controller;

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

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/systemEvent")
public class SystemEventController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventController.class);

    @Autowired private SystemEventDatastore systemEventDatastore;

    /**
     * Creates new SystemEvent.
     *  
     * @param SystemEvent
     */
    @RequestMapping(method=RequestMethod.POST)
    public GenericResponse create(@RequestBody SystemEventRecord systemEvent ) {

        LOG.debug("Creating SystemEventRecord {}", systemEvent);

        if (BaseJsonModel.hasUnsupportedValue(systemEvent)) {
            LOG.error("Failed to create SystemEventRecord, request contains unsupported value: {}", systemEvent);
            throw new DsDataValidationException("SystemEventRecord contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (systemEvent.getEventTimestamp() == 0) {
        	systemEvent.setEventTimestamp(ts);
        }

        systemEventDatastore.create(systemEvent);

        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("Created SystemEventRecord {}", systemEvent);

        return ret;
    }

    
    /**
     * Creates batch of new SystemEventRecords.
     *  
     * @param SystemEventRecord
     */
    @RequestMapping(value = "/bulk",method=RequestMethod.POST)
    public GenericResponse createBulk(@RequestBody List<SystemEventRecord> systemEvents ) {

    	if(systemEvents==null || systemEvents.isEmpty()) {
    		return new GenericResponse(true, "empty service_metrics_collection_config list");
    	}
    	
        LOG.debug("Creating SystemEventRecords {}", systemEvents.size());
                

        if (BaseJsonModel.hasUnsupportedValue(systemEvents)) {
            LOG.error("Failed to create SystemEventRecord, request contains unsupported value: {}", systemEvents);
            throw new DsDataValidationException("SystemEventRecord contains unsupported value");
        }

        long ts = System.currentTimeMillis();
		systemEvents.forEach(m -> {
			if (m.getEventTimestamp() == 0) {
				m.setEventTimestamp(ts);
			}
		});

        systemEventDatastore.create(systemEvents);

        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("Created SystemEventRecord {}", systemEvents.size());

        return ret;
    }


    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<SystemEventRecord> getForCustomer(@RequestParam long fromTime, @RequestParam long toTime, 
    		@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> locationIds, 
            @RequestParam(required = false) Set<Long> equipmentIds, 
            @RequestParam(required = false) Set<MacAddress> clientMacAdresses, 
    		@RequestParam(required = false) Set<String> dataTypes,
    		@RequestParam(required = false) List<ColumnAndSort> sortBy, 
    		@RequestParam(required = false) PaginationContext<SystemEventRecord> paginationContext) {    

    	if(paginationContext==null) {
    		paginationContext = new PaginationContext<>();
    	}
    	
        LOG.debug("Looking up SystemEventRecords for customer {} location {} equipment {} clientMac {} from {} to {} with last returned page number {}", 
                customerId, locationIds, equipmentIds, clientMacAdresses, fromTime, toTime, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<SystemEventRecord> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug("No more pages available when looking up SystemEventRecords");
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<SystemEventRecord> onePage = this.systemEventDatastore
                .getForCustomer(fromTime, toTime, customerId,
                		locationIds, equipmentIds, clientMacAdresses, dataTypes, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} SystemEventRecords", onePage.getItems().size());

        return ret;
    }
    
    
    /**
     * Deletes SystemEventRecord records
     * 
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public GenericResponse delete(@RequestParam int customerId, @RequestParam long equipmentId, @RequestParam long createdBeforeTimestamp) {
        
        LOG.debug("Deleting SystemEventRecords for  {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        systemEventDatastore.delete(customerId, equipmentId, createdBeforeTimestamp);

        LOG.debug("Deleted SystemEventRecords for  {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        return new GenericResponse(true, "");
    }
}
