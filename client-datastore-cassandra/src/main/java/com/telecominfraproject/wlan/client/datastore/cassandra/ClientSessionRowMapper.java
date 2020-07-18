package com.telecominfraproject.wlan.client.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;

/**
 * @author dtoptygin
 *
 */

public class ClientSessionRowMapper implements RowMapper<ClientSession> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientSessionRowMapper.class);

    public ClientSession mapRow(Row row)  {
    	ClientSession clientSession = new ClientSession();

    	clientSession.setCustomerId(row.getInt("customerId"));
    	clientSession.setMacAddress(new MacAddress(row.getLong("macAddress")));
        clientSession.setEquipmentId(row.getLong("equipmentId"));
        clientSession.setLocationId(row.getLong("locationId"));

        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;
        
        if (zippedBytes !=null) {
            try {
            	ClientSessionDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ClientSessionDetails.class);
                clientSession.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ClientSessionDetails from database for id {} {} {}", clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress());
            }
        }

        clientSession.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return clientSession;
    	
    }
}
