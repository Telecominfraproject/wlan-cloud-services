package com.telecominfraproject.wlan.routing.datastore.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */

public class RoutingRowMapper implements RowMapper<EquipmentRoutingRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(RoutingRowMapper.class);

    public EquipmentRoutingRecord mapRow(Row row)  {
    	EquipmentRoutingRecord routing = new EquipmentRoutingRecord();

        routing.setId(row.getLong("id"));

        routing.setCustomerId(row.getInt("customerId"));
        routing.setEquipmentId(row.getLong("equipmentId"));
        routing.setGatewayId(row.getLong("gatewayId"));

        routing.setCreatedTimestamp(row.getLong("createdTimestamp"));
        routing.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return routing;
    	
    }
}
