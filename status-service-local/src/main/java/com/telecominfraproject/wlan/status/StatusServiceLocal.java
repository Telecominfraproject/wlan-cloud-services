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
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@Component
public class StatusServiceLocal implements StatusServiceInterface {

    @Autowired private StatusController statusController;
    
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StatusServiceLocal.class);

    @Override
	public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
		return statusController.getOrNull(customerId, equipmentId, statusDataType);
	}


    @Override
	public List<Status> get(int customerId, long equipmentId) {
		return statusController.getForEquipment(customerId, equipmentId);
	}


    @Override
	public PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Status> paginationContext) {
		return statusController.getForCustomer(customerId, sortBy, paginationContext);
	}


    @Override
	public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
			Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy,
			PaginationContext<Status> paginationContext) {
		return statusController.getForCustomerWithFilter(customerId, equipmentIds, statusDataTypes, sortBy,
				paginationContext);
	}

    @Override
    public List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes) {
    	return statusController.getForEquipment(customerId, equipmentIds, statusDataTypes);
    }

    @Override
	public Status update(Status status) {
		return statusController.update(status);
	}


    @Override
	public List<Status> update(List<Status> statusList) {
		return statusController.updateBulk(statusList);
	}


    @Override
	public List<Status> delete(int customerId, long equipmentId) {
		return statusController.delete(customerId, equipmentId);
	}

}
