package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class SystemEventDatastoreRdbms implements SystemEventDatastore {

    @Autowired SystemEventDAO systemEventDAO;
    
    @Override
	public void create(SystemEventRecord systemEventRecord) {
    	systemEventDAO.create(systemEventRecord);
	}

	@Override
	public void create(List<SystemEventRecord> systemEventRecords) {
		systemEventDAO.create(systemEventRecords);		
	}

	@Override
	public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
		systemEventDAO.delete(customerId, equipmentId, createdBeforeTimestamp);
	}

	@Override
	public void delete(long createdBeforeTimestamp) {
		systemEventDAO.delete(createdBeforeTimestamp);		
	}

	@Override
	public PaginationResponse<SystemEventRecord> getForCustomer(long fromTime, long toTime, int customerId,
	        Set<Long> locationIds,
            Set<Long> equipmentIds, Set<MacAddress> clientMacAdresses, 	        
			Set<String> dataTypes, List<ColumnAndSort> sortBy,
			PaginationContext<SystemEventRecord> context) {
    	
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

    	return systemEventDAO.getForCustomer( fromTime, toTime, customerId, locationIds, 
    			equipmentIds, clientMacAdresses, dataTypes, sortBy,
    			context);
    }
}
