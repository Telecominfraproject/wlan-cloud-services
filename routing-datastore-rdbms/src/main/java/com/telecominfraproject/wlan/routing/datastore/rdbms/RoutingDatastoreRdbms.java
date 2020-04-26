package com.telecominfraproject.wlan.routing.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class RoutingDatastoreRdbms implements RoutingDatastore {

    @Autowired RoutingDAO routingDAO;

    @Override
    public Routing create(Routing routing) {
        return routingDAO.create(routing);
    }

    @Override
    public Routing get(long routingId) {
        return routingDAO.get(routingId);
    }

    @Override
    public Routing getOrNull(long routingId) {
        return routingDAO.getOrNull(routingId);
    }
    
    @Override
    public Routing update(Routing routing) {
        return routingDAO.update(routing);
    }

    @Override
    public Routing delete(long routingId) {
        return routingDAO.delete(routingId);
    }
    
    @Override
    public List<Routing> get(Set<Long> routingIdSet) {
    	return routingDAO.get(routingIdSet);
    }
    
    @Override
    public PaginationResponse<Routing> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Routing> context) {
    	return routingDAO.getForCustomer( customerId, sortBy, context);
    }
}
