package com.telecominfraproject.wlan.alarm.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;
import com.telecominfraproject.wlan.alarm.models.AlarmScopeType;
import com.telecominfraproject.wlan.alarm.models.OriginatorType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.status.models.StatusCode;

/**
 * @author dtoptygin
 *
 */

public class AlarmRowMapper implements RowMapper<Alarm> {
    
    private static final Logger LOG = LoggerFactory.getLogger(AlarmRowMapper.class);

    public Alarm mapRow(Row row)  {
        Alarm alarm = new Alarm();

        alarm.setCustomerId(row.getInt("customerId"));
        alarm.setEquipmentId(row.getLong("equipmentId"));
        alarm.setAlarmCode(AlarmCode.getById(row.getInt("alarmCode")));
        alarm.setCreatedTimestamp(row.getLong("createdTimestamp"));
        
        alarm.setOriginatorType(OriginatorType.getById(row.getInt("originatorType")));
        alarm.setSeverity(StatusCode.getById(row.getInt("severity")));
        alarm.setScopeType(AlarmScopeType.getById(row.getInt("scopeType")));
        alarm.setScopeId(row.getString("scopeId"));
        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;
        
        if (zippedBytes !=null) {
            try {
            	AlarmDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, AlarmDetails.class);
                alarm.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode AlarmDetails from database for {}", alarm);
            }
        }

        alarm.setAcknowledged(row.getBoolean("acknowledged"));
        alarm.setLastModifiedTimestamp(row.getLong("lastModifiedTimestamp"));
        
        return alarm;
    }
}
