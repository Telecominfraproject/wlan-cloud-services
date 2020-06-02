package com.telecominfraproject.wlan.portal.controller.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class StatusPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(StatusPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfStatuses extends ArrayList<Status> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private StatusServiceInterface statusServiceInterface;

    @RequestMapping(value = "/status/forEquipment", method = RequestMethod.GET)
    public ListOfStatuses getStatuses(@RequestParam int customerId, @RequestParam long equipmentId) {
        LOG.debug("Getting statuses {} {}", customerId, equipmentId);

        ListOfStatuses ret = new ListOfStatuses();
        
        List<Status> sList= statusServiceInterface.get(customerId, equipmentId);
        ret.addAll(sList);

        return ret;
    }

    @RequestMapping(value = "/status/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Status> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Status> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Statuses for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Status> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuses for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Status> onePage = this.statusServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Statuses for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }

    @RequestMapping(value = "/status/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Status> getForCustomerWithFilter(@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> equipmentIds,
            @RequestParam(required = false) Set<StatusDataType> statusDataTypes,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Status> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up status for customer {} equipment {} type {} last returned page number {}", 
                customerId, equipmentIds, statusDataTypes, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Status> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up status for customer {} equipment {}  type {} last returned page number {}",
                    customerId, equipmentIds, statusDataTypes, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Status> cePage = this.statusServiceInterface
                .getForCustomer(customerId, equipmentIds, statusDataTypes, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} statuses {} for customer {} equipment {}  type {}", cePage.getItems().size(), 
                customerId, equipmentIds, statusDataTypes);

        return ret;
    }    
    
}
