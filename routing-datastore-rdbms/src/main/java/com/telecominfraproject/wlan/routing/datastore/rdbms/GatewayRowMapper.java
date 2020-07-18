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
    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
        int colIdx=1;
        gateway.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties EquipmentGatewayRecord in here. 
        //make sure order of fields is the same as defined in EquipmentGatewayRecord
        gateway.setHostname(rs.getString(colIdx++));
        gateway.setIpAddr(rs.getString(colIdx++));
        gateway.setPort(rs.getInt(colIdx++));
        gateway.setGatewayType(GatewayType.getById(rs.getInt(colIdx++)));
        
        gateway.setCreatedTimestamp(rs.getLong(colIdx++));
        gateway.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return gateway;
    }
}