package com.telecominfraproject.wlan.portal.controller.equipment;

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

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class EquipmentPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfEquipment extends ArrayList<Equipment> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
    public Equipment getEquipment(@RequestParam long equipmentId) {
        LOG.debug("Getting equipment {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);

        return equipment;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.PUT)
    public Equipment updateEquipment(@RequestBody Equipment equipment) {
        LOG.debug("Updating equipment {}", equipment.getId());

        Equipment ret = equipmentServiceInterface.update(equipment);

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
    
    @RequestMapping(value = "/equipment/defaultDetails", method=RequestMethod.GET)
    public EquipmentDetails getDefaultEquipmentDetails(@RequestParam EquipmentType equipmentType) {
    	return this.equipmentServiceInterface.getDefaultEquipmentDetails(equipmentType);
    }
    
}
