package com.telecominfraproject.wlan.status.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author dtoptygin
 *
 */

public class StatusRowMapper implements RowMapper<Status> {
    
    private static final Logger LOG = LoggerFactory.getLogger(StatusRowMapper.class);

    public Status mapRow(Row row)  {
        Status status = new Status();

        status.setCustomerId(row.getInt("customerId"));
        status.setEquipmentId(row.getLong("equipmentId"));
        status.setStatusDataType(StatusDataType.getById(row.getInt("statusDataType")));
        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;
        
        if (zippedBytes !=null) {
            try {
            	StatusDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, StatusDetails.class);
                status.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode StatusDetails from database for {}:{}:{}", status.getCustomerId(), status.getEquipmentId(), status.getStatusDataType());
            }
        }

        status.setCreatedTimestamp(row.getLong("createdTimestamp"));
        status.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return status;
    	
    }
}
