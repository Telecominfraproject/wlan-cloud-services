package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;

/**
 * @author ekeddy
 *
 */
public class CustomerTrackAssignmentRowMapper implements RowMapper<CustomerFirmwareTrackRecord> {

    @Override
    public CustomerFirmwareTrackRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        int colIdx=1;
        CustomerFirmwareTrackRecord ret = new CustomerFirmwareTrackRecord(rs.getLong(colIdx++), rs.getInt(colIdx++));
        String settings = rs.getString(colIdx++);
        if(settings != null) {
            ret.setSettings(CustomerFirmwareTrackSettings.fromString(settings,CustomerFirmwareTrackSettings.class));
        }
        ret.setCreatedTimestamp(rs.getLong(colIdx++));
        ret.setLastModifiedTimestamp(rs.getLong(colIdx++));
        return ret;
    }

}
