package com.telecominfraproject.wlan.alarm.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;

/**
 * @author dtoptygin
 *
 */

public class AlarmRowMapper implements RowMapper<Alarm> {
    
    private static final Logger LOG = LoggerFactory.getLogger(AlarmRowMapper.class);

    public Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
        Alarm alarm = new Alarm();
        int colIdx=1;
        alarm.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Alarm in here. 
        //make sure order of fields is the same as defined in Alarm
        alarm.setCustomerId(rs.getInt(colIdx++));
        alarm.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	AlarmDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, AlarmDetails.class);
                alarm.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode AlarmDetails from database for id = {}", alarm.getId());
            }
        }

        alarm.setCreatedTimestamp(rs.getLong(colIdx++));
        alarm.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return alarm;
    }
}