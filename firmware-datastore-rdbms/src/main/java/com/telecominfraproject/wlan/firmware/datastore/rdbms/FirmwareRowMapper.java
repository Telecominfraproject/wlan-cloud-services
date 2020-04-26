package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.firmware.models.Firmware;
import com.telecominfraproject.wlan.firmware.models.FirmwareDetails;

/**
 * @author dtoptygin
 *
 */

public class FirmwareRowMapper implements RowMapper<Firmware> {
    
    private static final Logger LOG = LoggerFactory.getLogger(FirmwareRowMapper.class);

    public Firmware mapRow(ResultSet rs, int rowNum) throws SQLException {
        Firmware firmware = new Firmware();
        int colIdx=1;
        firmware.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Firmware in here. 
        //make sure order of fields is the same as defined in Firmware
        firmware.setCustomerId(rs.getInt(colIdx++));
        firmware.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	FirmwareDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, FirmwareDetails.class);
                firmware.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode FirmwareDetails from database for id = {}", firmware.getId());
            }
        }

        firmware.setCreatedTimestamp(rs.getLong(colIdx++));
        firmware.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return firmware;
    }
}