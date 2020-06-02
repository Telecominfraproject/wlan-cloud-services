package com.telecominfraproject.wlan.equipment.controller;

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
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.equipment.datastore.EquipmentDatastore;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentAddedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/equipment")
public class EquipmentController {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

    public static class ListOfEquipments extends ArrayList<Equipment> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private EquipmentDatastore equipmentDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Equipment.
     *  
     * @param Equipment
     * @return stored Equipment object
     * @throws RuntimeException if Equipment record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Equipment create(@RequestBody Equipment equipment ) {

        LOG.debug("Creating Equipment {}", equipment);

        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to create Equipment, request contains unsupported value: {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (equipment.getCreatedTimestamp() == 0) {
        	equipment.setCreatedTimestamp(ts);
        }
        equipment.setLastModifiedTimestamp(ts);

        Equipment ret = equipmentDatastore.create(equipment);

        LOG.debug("Created Equipment {}", ret);

        EquipmentAddedEvent event = new EquipmentAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Equipment by id
     * @param equipmentId
     * @return Equipment for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Equipment get(@RequestParam long equipmentId ) {
        
        LOG.debug("Retrieving Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.get(equipmentId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    /**
     * Retrieves Equipment by id
     * @param equipmentId
     * @return Equipment for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Equipment getOrNull(@RequestParam long equipmentId ) {
        
        LOG.debug("Retrieving Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.getOrNull(equipmentId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    /**
     * Retrieves Equipment by inventory id
     * @param inventory Id
     * @return Equipment for the supplied id
     */
    @RequestMapping(value = "/byInventoryIdOrNull", method=RequestMethod.GET)
    public Equipment getByInventoryIdOrNull(@RequestParam String inventoryId ) {
        
        LOG.debug("Retrieving Equipment {}", inventoryId);
        
        Equipment ret = equipmentDatastore.getByInventoryIdOrNull(inventoryId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfEquipments getAllInSet(@RequestParam Set<Long> equipmentIdSet) {
        LOG.debug("getAllInSet({})", equipmentIdSet);
        try {
            List<Equipment> result = equipmentDatastore.get(equipmentIdSet);
            LOG.debug("getAllInSet({}) return {} entries", equipmentIdSet, result.size());
            ListOfEquipments ret = new ListOfEquipments();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", equipmentIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Equipments for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Equipments for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Equipment> onePage = this.equipmentDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipments for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }

    @RequestMapping(value = "/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomerWithFilter(@RequestParam int customerId,
            @RequestParam EquipmentType equipmentType, @RequestParam Set<Long> locationIds,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up equipment {} for customer {} locations {} last returned page number {}", equipmentType,
                customerId, locationIds, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up equipment {} for customer {} locations {} last returned page number {}",
                    equipmentType, customerId, locationIds, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Equipment> cePage = this.equipmentDatastore
                .getForCustomer(customerId, equipmentType, locationIds, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} equipment {} for customer {} locations {} ", cePage.getItems().size(), equipmentType,
                customerId, locationIds);

        return ret;
    }

    /**
     * Updates Equipment record
     * 
     * @param Equipment
     * @return updated Equipment object
     * @throws RuntimeException if Equipment record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Equipment update(@RequestBody Equipment equipment){
        
        LOG.debug("Updating Equipment {}", equipment);
        
        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to update Equipment, request contains unsupported value: {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }

        Equipment ret = equipmentDatastore.update(equipment);

        LOG.debug("Updated Equipment {}", ret);

        EquipmentChangedEvent event = new EquipmentChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Equipment record
     * 
     * @param equipmentId
     * @return deleted Equipment object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Equipment delete(@RequestParam long equipmentId ) {
        
        LOG.debug("Deleting Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.delete(equipmentId);

        LOG.debug("Deleted Equipment {}", ret);
        
        EquipmentRemovedEvent event = new EquipmentRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    @RequestMapping(value = "/defaultDetails", method=RequestMethod.GET)
    public EquipmentDetails getDefaultEquipmentDetails(@RequestParam EquipmentType equipmentType) {
    	ApElementConfiguration apElementConfiguration = ApElementConfiguration.createWithDefaults();
    	return apElementConfiguration;
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
