package com.telecominfraproject.wlan.portal.controller.systemevent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.systemevent.SystemEventServiceInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class SystemEventPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfSystemEvents extends ArrayList<SystemEventRecord> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private SystemEventServiceInterface systemEventInterface;
    
    
    @RequestMapping(value = "/systemEvent/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<SystemEventRecord> getForCustomer(@RequestParam long fromTime, @RequestParam long toTime, 
    		@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> locationIds,
            @RequestParam(required = false) Set<Long> equipmentIds, 
            @RequestParam(required = false) Set<String> clientMacs, 
    		@RequestParam(required = false) Set<String> dataTypes,
    		@RequestParam(required = false) List<ColumnAndSort> sortBy, 
    		@RequestParam(required = false) PaginationContext<SystemEventRecord> paginationContext) {    

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up SystemEvents for customer {} equipment {} from {} to {} with last returned page number {}", 
                customerId, equipmentIds, fromTime, toTime, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<SystemEventRecord> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug("No more pages available when looking up SystemEvents for customer {} equipment {} from {} to {} with last returned page number {}", 
                    customerId, equipmentIds, fromTime, toTime, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        Set<MacAddress> macSet = new HashSet<>();

        if(clientMacs!=null) {
            clientMacs.forEach(m -> macSet.add(m!=null?new MacAddress(m):null));
        }

        PaginationResponse<SystemEventRecord> onePage = this.systemEventInterface
                .getForCustomer(fromTime, toTime, customerId,
                        locationIds, equipmentIds, macSet, dataTypes, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} SystemEvents for customer {} equipment {} from {} to {} ", onePage.getItems().size(), 
                customerId, equipmentIds, fromTime, toTime);

        return ret;
    }

    
}
