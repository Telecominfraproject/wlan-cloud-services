package com.telecominfraproject.wlan.client.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.ClientDetails;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;

/**
 * @author dtoptygin
 *
 */

public class ClientSessionRowMapper implements RowMapper<ClientSession> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientSessionRowMapper.class);

    public ClientSession mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClientSession clientSession = new ClientSession();
        int colIdx=1;
        clientSession.setMacAddress(new MacAddress(rs.getLong(colIdx++)));
        // macAddressString here does not need to map again to ClientSession Object
        colIdx++;
        
        //TODO: add columns from properties ClientSession in here. 
        //make sure order of fields is the same as defined in ClientSession
        clientSession.setCustomerId(rs.getInt(colIdx++));
        clientSession.setEquipmentId(rs.getLong(colIdx++));
        clientSession.setLocationId(rs.getLong(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	ClientSessionDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ClientSessionDetails.class);
                clientSession.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ClientSessionDetails from database for id {} {} {}", clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress());
            }
        }

        clientSession.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return clientSession;
    }
}