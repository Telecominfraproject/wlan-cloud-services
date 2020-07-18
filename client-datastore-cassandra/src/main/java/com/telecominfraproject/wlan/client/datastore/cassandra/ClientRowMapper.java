package com.telecominfraproject.wlan.client.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.ClientDetails;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;

/**
 * @author dtoptygin
 *
 */

public class ClientRowMapper implements RowMapper<Client> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientRowMapper.class);

    public Client mapRow(Row row)  {
    	Client client = new Client();

    	client.setCustomerId(row.getInt("customerId"));
    	client.setMacAddress(new MacAddress(row.getLong("macAddress")));
        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;
        
        if (zippedBytes !=null) {
            try {
            	ClientDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ClientDetails.class);
            	client.setDetails(details);
            } catch (RuntimeException exp) {
            	LOG.error("Failed to decode ClientDetails from database for id {} {}", client.getCustomerId(), client.getMacAddress());
            }
        }

        client.setCreatedTimestamp(row.getLong("createdTimestamp"));
        client.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return client;
    	
    }
}
