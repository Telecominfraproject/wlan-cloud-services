package com.telecominfraproject.wlan.portal.controller.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;
import com.telecominfraproject.wlan.status.StatusServiceInterface;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class EquipmentPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentPortalController.class);

    @Value("${tip.wlan.portal.equipment.numRetryUpdate:10}") 
    private int numRetryUpdate;
    
    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfEquipment extends ArrayList<Equipment> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @Autowired
    private AlarmServiceInterface alarmServiceInterface;

    @Autowired
    private StatusServiceInterface statusServiceInterface;

    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
    public Equipment getEquipment(@RequestParam long equipmentId) {
        LOG.debug("Getting equipment {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);

        return equipment;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.PUT)
    public Equipment updateEquipment(@RequestBody Equipment equipment) {
        LOG.debug("Updating equipment {}", equipment.getId());

        Equipment ret = null;

        for(int i=0; i<numRetryUpdate; i++) {
	        try {
	            ret = equipmentServiceInterface.update(equipment);
	            break;
	        } catch (DsConcurrentModificationException e) {
	            LOG.debug("Equipment was concurrently updated, retrying: {}", e.getMessage());
	            Equipment existing  = equipmentServiceInterface.getOrNull(equipment.getId());
	            equipment.setLastModifiedTimestamp(existing.getLastModifiedTimestamp());
	        }
        }

        if(ret == null) {
        	throw new DsConcurrentModificationException("Could not update equipment after " + numRetryUpdate + " retries");
        }
        
        return ret;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.POST)
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        LOG.debug("Creating equipment {}", equipment.getId());

        Equipment ret = equipmentServiceInterface.create(equipment);

        return ret;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.DELETE)
    public Equipment deleteEquipment(@RequestParam long equipmentId) {
        LOG.debug("Deleting equipment {}", equipmentId);

        Equipment ret = equipmentServiceInterface.delete(equipmentId);

        if(ret!= null && ret.getCustomerId() > 0) {
            try {
                alarmServiceInterface.delete(ret.getCustomerId(), equipmentId);
            }catch (RuntimeException e) {
                LOG.error("Cannot delete alarms for equipment {} ", equipmentId);
            }
            
            try {
                statusServiceInterface.delete(ret.getCustomerId(), equipmentId);
            }catch (RuntimeException e) {
                LOG.error("Cannot delete status for equipment {} ", equipmentId);
            }
            
        }

        return ret;
    }
    

    @RequestMapping(value = "/equipment/inSet", method = RequestMethod.GET)
    public ListOfEquipment getAllInSet(@RequestParam Set<Long> equipmentIdSet) {
        LOG.debug("getAllInSet({})", equipmentIdSet);
        try {
            List<Equipment> result = equipmentServiceInterface.get(equipmentIdSet);
            LOG.debug("getAllInSet({}) return {} entries", equipmentIdSet, result.size());
            ListOfEquipment ret = new ListOfEquipment();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", equipmentIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/equipment/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
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

        PaginationResponse<Equipment> onePage = this.equipmentServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipments for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    @RequestMapping(value = "/equipment/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomerWithFilter(@RequestParam int customerId,
            @RequestParam(required = false) EquipmentType equipmentType, 
            @RequestParam(required = false) Set<Long> locationIds,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
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

        PaginationResponse<Equipment> cePage = this.equipmentServiceInterface
                .getForCustomer(customerId, equipmentType, locationIds, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} equipment {} for customer {} locations {} ", cePage.getItems().size(), equipmentType,
                customerId, locationIds);

        return ret;
    }    
    
    @RequestMapping(value = "/searchByMacAndName", method = RequestMethod.GET)
    public PaginationResponse<Equipment> searchByMacAndName(@RequestParam int customerId,
            @RequestParam(required = false) String criteria,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up equipments for customer {} criteria {} last returned page number {}",
                customerId, criteria, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug("No more pages available when looking up equipments for customer {} criteria {} last returned page number {}",
                    customerId, criteria, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Equipment> cePage = this.equipmentServiceInterface
                .searchByMacAndName(customerId, criteria, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} equipments for customer {} criteria {} ", cePage.getItems().size(),
                customerId, criteria);

        return ret;
    }
    
    @RequestMapping(value = "/equipment/defaultDetails", method=RequestMethod.GET)
    public EquipmentDetails getDefaultEquipmentDetails(@RequestParam EquipmentType equipmentType) {
    	return this.equipmentServiceInterface.getDefaultEquipmentDetails(equipmentType);
    }

    @RequestMapping(value = "/equipment/rrmBulk", method=RequestMethod.PUT)
	public GenericResponse updateRrmBulk(@RequestBody EquipmentRrmBulkUpdateRequest request) {
        LOG.debug("updateRrmBulk {}", request);
        equipmentServiceInterface.updateRrmBulk(request);
        return new GenericResponse(true, "");
    }

}
