package com.telecominfraproject.wlan.routing.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */

public class RoutingRowMapper implements RowMapper<EquipmentRoutingRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(RoutingRowMapper.class);

    public EquipmentRoutingRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        EquipmentRoutingRecord routing = new EquipmentRoutingRecord();
        int colIdx=1;
        routing.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties EquipmentRoutingRecord in here. 
        //make sure order of fields is the same as defined in EquipmentRoutingRecord
        routing.setEquipmentId(rs.getLong(colIdx++));
        routing.setCustomerId(rs.getInt(colIdx++));
        routing.setGatewayId(rs.getLong(colIdx++));
        
        routing.setCreatedTimestamp(rs.getLong(colIdx++));
        routing.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return routing;
    }
}