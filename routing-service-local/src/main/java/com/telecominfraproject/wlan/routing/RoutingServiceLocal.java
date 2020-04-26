package com.telecominfraproject.wlan.routing;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.routing.controller.RoutingController;
import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@Component
public class RoutingServiceLocal implements RoutingServiceInterface {

    @Autowired private RoutingController routingController;
    private static final Logger LOG = LoggerFactory.getLogger(RoutingServiceLocal.class);

    
    @Override
    public Routing create(Routing routing) {
        LOG.debug("calling routingController.create {} ", routing);
        return routingController.create(routing);
    }

    @Override
    public Routing get(long routingId) {
        LOG.debug("calling routingController.get {} ", routingId);
        return routingController.get(routingId);
    }
    
    @Override
    public Routing getOrNull(long routingId) {
        LOG.debug("calling routingController.getOrNull {} ", routingId);
        return routingController.getOrNull(routingId);
    }
    
    @Override
    public List<Routing> get(Set<Long> routingIdSet) {
        LOG.debug("calling routingController.getAllInSet {} ", routingIdSet);
        return routingController.getAllInSet(routingIdSet);
    }
    
    @Override
    public PaginationResponse<Routing> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Routing> context) {
        LOG.debug("calling routingController.getForCustomer {} ", customerId);
        return routingController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Routing update(Routing routing) {
        LOG.debug("calling routingController.update {} ", routing);
        return routingController.update(routing);
    }

    @Override
    public Routing delete(long routingId) {
        LOG.debug("calling routingController.delete {} ", routingId);
        return routingController.delete(routingId);
    }

}
