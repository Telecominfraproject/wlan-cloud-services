package com.telecominfraproject.wlan.routing.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class RoutingDatastoreRdbms implements RoutingDatastore {

    @Autowired RoutingDAO routingDAO;
    @Autowired GatewayDAO gatewayDAO;

    @Override
    public EquipmentRoutingRecord create(EquipmentRoutingRecord routing) {
        return routingDAO.create(routing);
    }

    @Override
    public EquipmentRoutingRecord get(long routingId) {
        return routingDAO.get(routingId);
    }

    @Override
    public EquipmentRoutingRecord getOrNull(long routingId) {
        return routingDAO.getOrNull(routingId);
    }
    
    @Override
    public EquipmentRoutingRecord update(EquipmentRoutingRecord routing) {
        return routingDAO.update(routing);
    }

    @Override
    public EquipmentRoutingRecord delete(long routingId) {
        return routingDAO.delete(routingId);
    }
    
    @Override
    public List<EquipmentRoutingRecord> get(Set<Long> routingIdSet) {
    	return routingDAO.get(routingIdSet);
    }
    
    @Override
    public PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<EquipmentRoutingRecord> context) {
    	
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

    	return routingDAO.getForCustomer( customerId, sortBy, context);
    }

	@Override
	public EquipmentGatewayRecord registerGateway(EquipmentGatewayRecord equipmentGatewayRecord) {
		return gatewayDAO.create(equipmentGatewayRecord);
	}

	@Override
	public EquipmentGatewayRecord getGateway(long id) {
		return gatewayDAO.get(id);
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(String hostname) {
		return gatewayDAO.getGateway(hostname);
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
		return gatewayDAO.getGateway(gatewayType);
	}

	@Override
	public List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId) {
		return routingDAO.getRegisteredRouteList(equipmentId);
	}

	@Override
	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
		return gatewayDAO.getRegisteredGatewayRecordList(equipmentId);
	}

	@Override
	public EquipmentGatewayRecord updateGateway(EquipmentGatewayRecord equipmentGwRecord) {
		return gatewayDAO.update(equipmentGwRecord);
	}

	@Override
	public EquipmentGatewayRecord deleteGateway(long id) {
		return gatewayDAO.delete(id);
	}

	@Override
	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
		return gatewayDAO.deleteGateway(hostname);
	}

}
