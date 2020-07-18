package com.telecominfraproject.wlan.routing.datastore.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;

/**
 * @author dtoptygin
 *
 */

public class GatewayRowMapper implements RowMapper<EquipmentGatewayRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(GatewayRowMapper.class);

    public EquipmentGatewayRecord mapRow(Row row)  {
    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();

    	gateway.setId(row.getLong("id"));

        //TODO: add columns from properties EquipmentGatewayRecord in here. 
        gateway.setHostname(row.getString("hostname"));
        gateway.setIpAddr(row.getString("ipAddr"));
        gateway.setPort(row.getInt("port"));
        gateway.setGatewayType(GatewayType.getById(row.getInt("gatewayType")));
        
        gateway.setCreatedTimestamp(row.getLong("createdTimestamp"));
        gateway.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return gateway;
        
    }
}
