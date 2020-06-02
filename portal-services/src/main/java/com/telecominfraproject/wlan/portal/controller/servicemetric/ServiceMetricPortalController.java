package com.telecominfraproject.wlan.portal.controller.servicemetric;

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
import com.telecominfraproject.wlan.servicemetric.ServiceMetricServiceInterface;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class ServiceMetricPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfServiceMetrics extends ArrayList<ServiceMetric> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private ServiceMetricServiceInterface serviceMetricInterface;
    
    
    @RequestMapping(value = "/serviceMetric/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<ServiceMetric> getForCustomer(@RequestParam long fromTime, @RequestParam long toTime, 
    		@RequestParam int customerId,
    		@RequestParam(required = false) Set<Long> equipmentIds, 
    		@RequestParam(required = false) Set<String> clientMacs, 
    		@RequestParam(required = false) Set<ServiceMetricDataType> dataTypes,
    		@RequestParam(required = false) List<ColumnAndSort> sortBy, 
    		@RequestParam(required = false) PaginationContext<ServiceMetric> paginationContext) {    

        LOG.debug("Looking up ServiceMetrics for customer {} equipment {} from {} to {} with last returned page number {}", 
                customerId, equipmentIds, fromTime, toTime, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug("No more pages available when looking up ServiceMetrics for customer {} equipment {} from {} to {} with last returned page number {}", 
                    customerId, equipmentIds, fromTime, toTime, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

    	Set<MacAddress> macSet = new HashSet<>();

        if(clientMacs!=null) {
	    	clientMacs.forEach(m -> macSet.add(m!=null?new MacAddress(m):null));
        }

        PaginationResponse<ServiceMetric> onePage = this.serviceMetricInterface
                .getForCustomer(fromTime, toTime, customerId,
                		equipmentIds, macSet, dataTypes, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} ServiceMetrics for customer {} equipment {} from {} to {} ", onePage.getItems().size(), 
                customerId, equipmentIds, fromTime, toTime);

        return ret;
    }

    
}
