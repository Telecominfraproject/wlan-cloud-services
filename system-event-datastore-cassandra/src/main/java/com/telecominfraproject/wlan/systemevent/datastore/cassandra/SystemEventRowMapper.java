package com.telecominfraproject.wlan.systemevent.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.UnserializableSystemEvent;

/**
 * @author dtoptygin
 *
 */

public class SystemEventRowMapper implements RowMapper<SystemEventRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(SystemEventRowMapper.class);

    public SystemEventRecord mapRow(Row row)  {
        SystemEventRecord systemEventRecord = new SystemEventRecord();

        systemEventRecord.setCustomerId(row.getInt("customerId"));
        systemEventRecord.setEquipmentId(row.getLong("equipmentId"));

        systemEventRecord.setDataType(row.getString("dataType"));
        systemEventRecord.setEventTimestamp(row.getLong("eventTimestamp"));
        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;

        if (zippedBytes !=null) {
            try {
            	SystemEvent details = BaseJsonModel.fromZippedBytes(zippedBytes, SystemEvent.class);
                systemEventRecord.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode SystemEvent from database for {}", systemEventRecord);
                try {
	                //cannot parse this event (because current code does not have this class anymore?)
	                //so we'll deliver unparsed string to the caller
	                UnserializableSystemEvent evt = new UnserializableSystemEvent();
	                evt.setCustomerId(systemEventRecord.getCustomerId());
	                evt.setEquipmentId(systemEventRecord.getEquipmentId());
	                evt.setEventTimestamp(systemEventRecord.getEventTimestamp());
	                evt.setPayload(SystemEvent.fromZippedBytesAsString(zippedBytes));
	                systemEventRecord.setDetails(evt);
                } catch (RuntimeException e) {
                    LOG.error("Failed to parse UnserializableSystemEvent for {}", systemEventRecord);
                }
            }
        }
        
        return systemEventRecord;
    	
    }
}
