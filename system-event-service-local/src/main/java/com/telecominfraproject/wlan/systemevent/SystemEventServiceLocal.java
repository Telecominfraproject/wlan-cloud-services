package com.telecominfraproject.wlan.systemevent;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.systemevent.controller.SystemEventController;
import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 *
 */
@Component
public class SystemEventServiceLocal implements SystemEventServiceInterface {

    @Autowired private SystemEventController systemEventController;
    private static final Logger LOG = LoggerFactory.getLogger(SystemEventServiceLocal.class);

    
    @Override
    public SystemEventContainer create(SystemEventContainer systemEventRecord) {
        LOG.debug("calling systemEventController.create {} ", systemEventRecord);
        return systemEventController.create(systemEventRecord);
    }

    @Override
    public SystemEventContainer get(long systemEventRecordId) {
        LOG.debug("calling systemEventController.get {} ", systemEventRecordId);
        return systemEventController.get(systemEventRecordId);
    }
    
    @Override
    public SystemEventContainer getOrNull(long systemEventRecordId) {
        LOG.debug("calling systemEventController.getOrNull {} ", systemEventRecordId);
        return systemEventController.getOrNull(systemEventRecordId);
    }
    
    @Override
    public List<SystemEventContainer> get(Set<Long> systemEventRecordIdSet) {
        LOG.debug("calling systemEventController.getAllInSet {} ", systemEventRecordIdSet);
        return systemEventController.getAllInSet(systemEventRecordIdSet);
    }
    
    @Override
    public PaginationResponse<SystemEventContainer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<SystemEventContainer> context) {
        LOG.debug("calling systemEventController.getForCustomer {} ", customerId);
        return systemEventController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public SystemEventContainer update(SystemEventContainer systemEventRecord) {
        LOG.debug("calling systemEventController.update {} ", systemEventRecord);
        return systemEventController.update(systemEventRecord);
    }

    @Override
    public SystemEventContainer delete(long systemEventRecordId) {
        LOG.debug("calling systemEventController.delete {} ", systemEventRecordId);
        return systemEventController.delete(systemEventRecordId);
    }

}
