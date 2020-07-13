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
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class StatusDatastoreRdbms implements StatusDatastore {

    @Autowired StatusDAO statusDAO;

    @Override
    public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
        return statusDAO.getOrNull(customerId, equipmentId, statusDataType);
    }
    
    @Override
    public Status update(Status status) {
        return statusDAO.update(status);
    }
    
    @Override
    public List<Status> delete(int customerId, long equipmentId) {
        return statusDAO.delete(customerId, equipmentId);
    }
    
    @Override
    public List<Status> get(int customerId, long equipmentId) {
    	return statusDAO.get(customerId, equipmentId);
    }
        
    @Override
    public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
    		Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy, PaginationContext<Status> context) {
    	
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

    	return statusDAO.getForCustomer( customerId, equipmentIds, statusDataTypes, sortBy, context);
    }
    
    @Override
    public List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes) {
    	return statusDAO.getForEquipment(customerId, equipmentIds, statusDataTypes);
    }
}
