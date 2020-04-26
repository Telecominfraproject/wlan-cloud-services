package com.telecominfraproject.wlan.status.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class StatusDatastoreRdbms implements StatusDatastore {

    @Autowired StatusDAO statusDAO;

    @Override
    public Status create(Status status) {
        return statusDAO.create(status);
    }

    @Override
    public Status get(long statusId) {
        return statusDAO.get(statusId);
    }

    @Override
    public Status getOrNull(long statusId) {
        return statusDAO.getOrNull(statusId);
    }
    
    @Override
    public Status update(Status status) {
        return statusDAO.update(status);
    }

    @Override
    public Status delete(long statusId) {
        return statusDAO.delete(statusId);
    }
    
    @Override
    public List<Status> get(Set<Long> statusIdSet) {
    	return statusDAO.get(statusIdSet);
    }
    
    @Override
    public PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Status> context) {
    	return statusDAO.getForCustomer( customerId, sortBy, context);
    }
}
