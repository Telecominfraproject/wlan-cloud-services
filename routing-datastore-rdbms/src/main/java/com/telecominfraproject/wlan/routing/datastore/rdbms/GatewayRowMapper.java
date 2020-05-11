package com.telecominfraproject.wlan.routing.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;

/**
 * @author dtoptygin
 *
 */

public class GatewayRowMapper implements RowMapper<EquipmentGatewayRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(GatewayRowMapper.class);

    public EquipmentGatewayRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
    	EquipmentGatewayRecord routing = new EquipmentGatewayRecord();
        int colIdx=1;
        routing.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties EquipmentGatewayRecord in here. 
        //make sure order of fields is the same as defined in EquipmentGatewayRecord
        routing.setHostname(rs.getString(colIdx++));
        routing.setIpAddr(rs.getString(colIdx++));
        routing.setPort(rs.getInt(colIdx++));
        routing.setGatewayType(GatewayType.getById(rs.getInt(colIdx++)));
        
        routing.setCreatedTimestamp(rs.getLong(colIdx++));
        routing.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return routing;
    }
}