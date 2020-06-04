package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.UnserializableSystemEvent;

/**
 * @author dtoptygin
 *
 */

public class SystemEventRowMapper implements RowMapper<SystemEventRecord> {
    
    private static final Logger LOG = LoggerFactory.getLogger(SystemEventRowMapper.class);

    public SystemEventRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        SystemEventRecord systemEventRecord = new SystemEventRecord();
        int colIdx=1;

        systemEventRecord.setCustomerId(rs.getInt(colIdx++));
        systemEventRecord.setEquipmentId(rs.getLong(colIdx++));
        systemEventRecord.setDataType(rs.getString(colIdx++));
        systemEventRecord.setEventTimestamp(rs.getLong(colIdx++));
        
        //TODO: add columns from properties SystemEventRecord in here. 
        //make sure order of fields is the same as defined in SystemEventRecord

        byte[] zippedBytes = rs.getBytes(colIdx++);
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