package com.telecominfraproject.wlan.systemevent;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.systemevent.controller.SystemEventController;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventStats;

/**
 * @author dtoptygin
 *
 */
@Component
public class SystemEventServiceLocal implements SystemEventServiceInterface {

    @Autowired private SystemEventController systemEventController;
    private static final Logger LOG = LoggerFactory.getLogger(SystemEventServiceLocal.class);

    
    @Override
	public GenericResponse create(SystemEventRecord systemEventRecord) {
        LOG.debug("calling systemEventController.create {} ", systemEventRecord);
        return systemEventController.create(systemEventRecord);		
	}

	@Override
	public GenericResponse create(List<SystemEventRecord> systemEventRecords) {
        LOG.debug("calling systemEventController.create bulk {} ", systemEventRecords.size());
        return systemEventController.createBulk(systemEventRecords);		
	}

	@Override
	public GenericResponse delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        LOG.debug("calling systemEventController.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        return systemEventController.delete(customerId, equipmentId, createdBeforeTimestamp);
	}
	
	@Override
	public GenericResponse delete(long createdBeforeTimestamp) {
		LOG.debug("calling systemEventController.delete {}", createdBeforeTimestamp);
		return systemEventController.delete(createdBeforeTimestamp);
	}

	@Override
	public PaginationResponse<SystemEventRecord> getForCustomer(long fromTime, long toTime, int customerId,
	        Set<Long> locationIds, Set<Long> equipmentIds, Set<MacAddress> clientMacAdresses, Set<String> dataTypes, List<ColumnAndSort> sortBy,
			PaginationContext<SystemEventRecord> context) {
		
        LOG.debug("calling systemEventController.getForCustomer {} {} {} ", fromTime, toTime, customerId);

        return systemEventController.getForCustomer(fromTime, toTime, customerId,
                locationIds, equipmentIds, clientMacAdresses, dataTypes, sortBy, context);
	}

    @Override
    public SystemEventStats getSystemEventStats(String filterAttributeName, String filterAttributeValue, long fromTime, long toTime) {
        LOG.debug("calling systemEventController.getSystemEventStats {} {} {} {}", filterAttributeName, filterAttributeValue, fromTime, toTime);

        return systemEventController.getSystemEventStats(filterAttributeName, filterAttributeValue, fromTime, toTime);
    }

}
