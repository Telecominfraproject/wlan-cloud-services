package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class SystemEventDatastoreRdbms implements SystemEventDatastore {

    @Autowired SystemEventDAO systemEventDAO;

    @Override
    public SystemEventContainer create(SystemEventContainer systemEventRecord) {
        return systemEventDAO.create(systemEventRecord);
    }

    @Override
    public SystemEventContainer get(long systemEventRecordId) {
        return systemEventDAO.get(systemEventRecordId);
    }

    @Override
    public SystemEventContainer getOrNull(long systemEventRecordId) {
        return systemEventDAO.getOrNull(systemEventRecordId);
    }
    
    @Override
    public SystemEventContainer update(SystemEventContainer systemEventRecord) {
        return systemEventDAO.update(systemEventRecord);
    }

    @Override
    public SystemEventContainer delete(long systemEventRecordId) {
        return systemEventDAO.delete(systemEventRecordId);
    }
    
    @Override
    public List<SystemEventContainer> get(Set<Long> systemEventRecordIdSet) {
    	return systemEventDAO.get(systemEventRecordIdSet);
    }
    
    @Override
    public PaginationResponse<SystemEventContainer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<SystemEventContainer> context) {
    	return systemEventDAO.getForCustomer( customerId, sortBy, context);
    }
}
