package com.telecominfraproject.wlan.status;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.status.controller.StatusController;
import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 *
 */
@Component
public class StatusServiceLocal implements StatusServiceInterface {

    @Autowired private StatusController statusController;
    private static final Logger LOG = LoggerFactory.getLogger(StatusServiceLocal.class);

    
    @Override
    public Status create(Status status) {
        LOG.debug("calling statusController.create {} ", status);
        return statusController.create(status);
    }

    @Override
    public Status get(long statusId) {
        LOG.debug("calling statusController.get {} ", statusId);
        return statusController.get(statusId);
    }
    
    @Override
    public Status getOrNull(long statusId) {
        LOG.debug("calling statusController.getOrNull {} ", statusId);
        return statusController.getOrNull(statusId);
    }
    
    @Override
    public List<Status> get(Set<Long> statusIdSet) {
        LOG.debug("calling statusController.getAllInSet {} ", statusIdSet);
        return statusController.getAllInSet(statusIdSet);
    }
    
    @Override
    public PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Status> context) {
        LOG.debug("calling statusController.getForCustomer {} ", customerId);
        return statusController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Status update(Status status) {
        LOG.debug("calling statusController.update {} ", status);
        return statusController.update(status);
    }

    @Override
    public Status delete(long statusId) {
        LOG.debug("calling statusController.delete {} ", statusId);
        return statusController.delete(statusId);
    }

}
