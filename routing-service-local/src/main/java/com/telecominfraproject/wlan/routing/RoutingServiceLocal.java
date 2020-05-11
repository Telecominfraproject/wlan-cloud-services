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
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.routing.controller.RoutingController;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
@Component
public class RoutingServiceLocal implements RoutingServiceInterface {

    @Autowired private RoutingController routingController;
    private static final Logger LOG = LoggerFactory.getLogger(RoutingServiceLocal.class);

    
    @Override
    public EquipmentRoutingRecord create(EquipmentRoutingRecord routing) {
        LOG.debug("calling routingController.create {} ", routing);
        return routingController.create(routing);
    }

    @Override
    public EquipmentRoutingRecord get(long routingId) {
        LOG.debug("calling routingController.get {} ", routingId);
        return routingController.get(routingId);
    }
    
    @Override
    public EquipmentRoutingRecord getOrNull(long routingId) {
        LOG.debug("calling routingController.getOrNull {} ", routingId);
        return routingController.getOrNull(routingId);
    }
    
    @Override
    public List<EquipmentRoutingRecord> get(Set<Long> routingIdSet) {
        LOG.debug("calling routingController.getAllInSet {} ", routingIdSet);
        return routingController.getAllInSet(routingIdSet);
    }
    
    @Override
    public PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<EquipmentRoutingRecord> context) {
        LOG.debug("calling routingController.getForCustomer {} ", customerId);
        return routingController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public EquipmentRoutingRecord update(EquipmentRoutingRecord routing) {
        LOG.debug("calling routingController.update {} ", routing);
        return routingController.update(routing);
    }

    @Override
    public EquipmentRoutingRecord delete(long routingId) {
        LOG.debug("calling routingController.delete {} ", routingId);
        return routingController.delete(routingId);
    }

	@Override
	public EquipmentGatewayRecord registerGateway(EquipmentGatewayRecord equipmentGwRecord) {
		return routingController.registerGateway(equipmentGwRecord);
	}

	@Override
	public EquipmentGatewayRecord getGateway(long id) {
		return routingController.getGatewayById(id);
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(String hostname) {
		return routingController.getGatewayByHostname(hostname);
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
		return routingController.getGatewayByType(gatewayType);
	}

	@Override
	public List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId) {
		return routingController.getRegisteredRouteList(equipmentId);
	}

	@Override
	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
		return routingController.getRegisteredGatewayRecordList(equipmentId);
	}

	@Override
	public EquipmentGatewayRecord updateGateway(EquipmentGatewayRecord equipmentGwRecord) {
		return routingController.updateGateway(equipmentGwRecord);
	}

	@Override
	public EquipmentGatewayRecord deleteGateway(long id) {
		return routingController.deleteGateway(id);
	}

	@Override
	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
		return routingController.deleteGateway(hostname);
	}

}
