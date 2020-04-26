package com.telecominfraproject.wlan.client.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.ClientDetails;

/**
 * @author dtoptygin
 *
 */

public class ClientRowMapper implements RowMapper<Client> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientRowMapper.class);

    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        int colIdx=1;
        client.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Client in here. 
        //make sure order of fields is the same as defined in Client
        client.setCustomerId(rs.getInt(colIdx++));
        client.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	ClientDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ClientDetails.class);
                client.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ClientDetails from database for id = {}", client.getId());
            }
        }

        client.setCreatedTimestamp(rs.getLong(colIdx++));
        client.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return client;
    }
}