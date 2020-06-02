package com.telecominfraproject.wlan.routing.controller;

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
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;
import com.telecominfraproject.wlan.routing.models.events.GatewayAddedEvent;
import com.telecominfraproject.wlan.routing.models.events.GatewayChangedEvent;
import com.telecominfraproject.wlan.routing.models.events.GatewayRemovedEvent;
import com.telecominfraproject.wlan.routing.models.events.RoutingAddedEvent;
import com.telecominfraproject.wlan.routing.models.events.RoutingChangedEvent;
import com.telecominfraproject.wlan.routing.models.events.RoutingRemovedEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/routing")
public class RoutingController {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingController.class);

    public static class ListOfRoutings extends ArrayList<EquipmentRoutingRecord> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    public static class ListOfGateways extends ArrayList<EquipmentGatewayRecord> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private RoutingDatastore routingDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new EquipmentRoutingRecord.
     *  
     * @param EquipmentRoutingRecord
     * @return stored EquipmentRoutingRecord object
     * @throws RuntimeException if EquipmentRoutingRecord record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public EquipmentRoutingRecord create(@RequestBody EquipmentRoutingRecord routing ) {

        LOG.debug("Creating EquipmentRoutingRecord {}", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to create EquipmentRoutingRecord, request contains unsupported value: {}", routing);
            throw new DsDataValidationException("EquipmentRoutingRecord contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (routing.getCreatedTimestamp() == 0) {
        	routing.setCreatedTimestamp(ts);
        }
        routing.setLastModifiedTimestamp(ts);

        EquipmentRoutingRecord ret = routingDatastore.create(routing);

        LOG.debug("Created EquipmentRoutingRecord {}", ret);

        RoutingAddedEvent event = new RoutingAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves EquipmentRoutingRecord by id
     * @param routingId
     * @return EquipmentRoutingRecord for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public EquipmentRoutingRecord get(@RequestParam long routingId ) {
        
        LOG.debug("Retrieving EquipmentRoutingRecord {}", routingId);
        
        EquipmentRoutingRecord ret = routingDatastore.get(routingId);

        LOG.debug("Retrieved EquipmentRoutingRecord {}", ret);

        return ret;
    }

    /**
     * Retrieves EquipmentRoutingRecord by id
     * @param routingId
     * @return EquipmentRoutingRecord for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public EquipmentRoutingRecord getOrNull(@RequestParam long routingId ) {
        
        LOG.debug("Retrieving EquipmentRoutingRecord {}", routingId);
        
        EquipmentRoutingRecord ret = routingDatastore.getOrNull(routingId);

        LOG.debug("Retrieved EquipmentRoutingRecord {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfRoutings getAllInSet(@RequestParam Set<Long> routingIdSet) {
        LOG.debug("getAllInSet({})", routingIdSet);
        try {
            List<EquipmentRoutingRecord> result = routingDatastore.get(routingIdSet);
            LOG.debug("getAllInSet({}) return {} entries", routingIdSet, result.size());
            ListOfRoutings ret = new ListOfRoutings();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", routingIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<EquipmentRoutingRecord> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<EquipmentRoutingRecord> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Routings for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<EquipmentRoutingRecord> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Routings for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<EquipmentRoutingRecord> onePage = this.routingDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Routings for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates EquipmentRoutingRecord record
     * 
     * @param EquipmentRoutingRecord
     * @return updated EquipmentRoutingRecord object
     * @throws RuntimeException if EquipmentRoutingRecord record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public EquipmentRoutingRecord update(@RequestBody EquipmentRoutingRecord routing){
        
        LOG.debug("Updating EquipmentRoutingRecord {}", routing);
        
        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to update EquipmentRoutingRecord, request contains unsupported value: {}", routing);
            throw new DsDataValidationException("EquipmentRoutingRecord contains unsupported value");
        }

        EquipmentRoutingRecord ret = routingDatastore.update(routing);

        LOG.debug("Updated EquipmentRoutingRecord {}", ret);

        RoutingChangedEvent event = new RoutingChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes EquipmentRoutingRecord record
     * 
     * @param routingId
     * @return deleted EquipmentRoutingRecord object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public EquipmentRoutingRecord delete(@RequestParam long routingId ) {
        
        LOG.debug("Deleting EquipmentRoutingRecord {}", routingId);
        
        EquipmentRoutingRecord ret = routingDatastore.delete(routingId);

        LOG.debug("Deleted EquipmentRoutingRecord {}", ret);
        
        RoutingRemovedEvent event = new RoutingRemovedEvent(ret);
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

    @RequestMapping(value = "/gateway", method=RequestMethod.POST)
	public EquipmentGatewayRecord registerGateway(@RequestBody EquipmentGatewayRecord equipmentGwRecord) {

        LOG.debug("Creating EquipmentGatewayRecord {}", equipmentGwRecord);

        if (BaseJsonModel.hasUnsupportedValue(equipmentGwRecord)) {
            LOG.error("Failed to create EquipmentGatewayRecord, request contains unsupported value: {}", equipmentGwRecord);
            throw new DsDataValidationException("EquipmentGatewayRecord contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (equipmentGwRecord.getCreatedTimestamp() == 0) {
        	equipmentGwRecord.setCreatedTimestamp(ts);
        }
        equipmentGwRecord.setLastModifiedTimestamp(ts);

        EquipmentGatewayRecord ret = routingDatastore.registerGateway(equipmentGwRecord);

        LOG.debug("Created EquipmentGatewayRecord {}", ret);

        GatewayAddedEvent event = new GatewayAddedEvent(ret);
        publishEvent(event);


        return ret;
    }	

    @RequestMapping(value = "/gateway", method=RequestMethod.GET)
	public EquipmentGatewayRecord getGatewayById(@RequestParam long gatewayId) {
        
        LOG.debug("Retrieving EquipmentGatewayRecord {}", gatewayId);
        
        EquipmentGatewayRecord ret = routingDatastore.getGateway(gatewayId);

        LOG.debug("Retrieved EquipmentGatewayRecord {}", ret);

        return ret;
	}

    @RequestMapping(value = "/gateway/byHostname", method=RequestMethod.GET)
	public ListOfGateways getGatewayByHostname(@RequestParam String hostname) {
        LOG.debug("Retrieving EquipmentGatewayRecord {}", hostname);
        
        ListOfGateways ret = new ListOfGateways();
        List<EquipmentGatewayRecord> gateways = routingDatastore.getGateway(hostname);
        ret.addAll(gateways);

        LOG.debug("Retrieved EquipmentGatewayRecord {}", ret);

        return ret;
	}

    @RequestMapping(value = "/gateway/byType", method=RequestMethod.GET)
	public ListOfGateways getGatewayByType(@RequestParam GatewayType gatewayType) {
        LOG.debug("Retrieving EquipmentGatewayRecord by type {}", gatewayType);
        
        ListOfGateways ret = new ListOfGateways();
        List<EquipmentGatewayRecord> gateways = routingDatastore.getGateway(gatewayType);
        ret.addAll(gateways);

        LOG.debug("Retrieved EquipmentGatewayRecord {}", ret);

        return ret;
	}

    @RequestMapping(value = "/byEquipmentId", method=RequestMethod.GET)
	public ListOfRoutings getRegisteredRouteList(@RequestParam long equipmentId) {
        LOG.debug("Retrieving EquipmentRoutingRecord by equipment id {}", equipmentId);
        
        ListOfRoutings ret = new ListOfRoutings();
        List<EquipmentRoutingRecord> routes = routingDatastore.getRegisteredRouteList(equipmentId);
        ret.addAll(routes);

        LOG.debug("Retrieved EquipmentRoutingRecord {}", ret);

        return ret;
	}

    @RequestMapping(value = "/gateway/byEquipmentId", method=RequestMethod.GET)
	public ListOfGateways getRegisteredGatewayRecordList(@RequestParam long equipmentId) {
        LOG.debug("Retrieving EquipmentGatewayRecord by equipment id {}", equipmentId);
        
        ListOfGateways ret = new ListOfGateways();
        List<EquipmentGatewayRecord> gateways = routingDatastore.getRegisteredGatewayRecordList(equipmentId);
        ret.addAll(gateways);

        LOG.debug("Retrieved EquipmentGatewayRecord {}", ret);

        return ret;
	}

    @RequestMapping(value = "/gateway", method=RequestMethod.PUT)
	public EquipmentGatewayRecord updateGateway(@RequestBody EquipmentGatewayRecord equipmentGwRecord) {

    	LOG.debug("Updating EquipmentGatewayRecord {}", equipmentGwRecord);
        
        if (BaseJsonModel.hasUnsupportedValue(equipmentGwRecord)) {
            LOG.error("Failed to update EquipmentGatewayRecord, request contains unsupported value: {}", equipmentGwRecord);
            throw new DsDataValidationException("EquipmentGatewayRecord contains unsupported value");
        }

        EquipmentGatewayRecord ret = routingDatastore.updateGateway(equipmentGwRecord);

        LOG.debug("Updated EquipmentGatewayRecord {}", ret);

        GatewayChangedEvent event = new GatewayChangedEvent(ret);
        publishEvent(event);

        return ret;
    }

    @RequestMapping(value = "/gateway", method=RequestMethod.DELETE)
	public EquipmentGatewayRecord deleteGateway(@RequestParam long gatewayId) {
        
        LOG.debug("Deleting EquipmentGatewayRecord {}", gatewayId);
        
        EquipmentGatewayRecord ret = routingDatastore.deleteGateway(gatewayId);

        LOG.debug("Deleted EquipmentGatewayRecord {}", ret);
        
        GatewayRemovedEvent event = new GatewayRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    @RequestMapping(value = "/gateway/byHostname", method=RequestMethod.DELETE)
	public ListOfGateways deleteGateway(@RequestParam String hostname) {
        LOG.debug("Deleting EquipmentGatewayRecord by hostname {}", hostname);
        
        ListOfGateways ret = new ListOfGateways();
        List<EquipmentGatewayRecord> gateways = routingDatastore.deleteGateway(hostname);
        
        if(gateways!=null) {
        	gateways.forEach(g -> ret.add(g.clone()));
        }
        
        long now = System.currentTimeMillis();
        ret.forEach(r -> r.setLastModifiedTimestamp(now) );
        
        LOG.debug("Deleted EquipmentGatewayRecords {}", ret);
        
        List<SystemEventRecord> events = new ArrayList<>();
        ret.forEach(r -> events.add(new SystemEventRecord(new GatewayRemovedEvent(r) )));

        if(!events.isEmpty()) {
	        try {
	            cloudEventDispatcher.publishEvents(events);
	        } catch (Exception e) {
	            LOG.error("Failed to publish event : {}", events, e);
	        }
        }
        
        return ret;
	}

}
