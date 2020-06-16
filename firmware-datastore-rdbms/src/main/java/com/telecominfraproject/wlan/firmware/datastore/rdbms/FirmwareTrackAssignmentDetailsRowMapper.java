package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;

public class FirmwareTrackAssignmentDetailsRowMapper implements RowMapper<FirmwareTrackAssignmentDetails> {

    @Override
    public FirmwareTrackAssignmentDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        FirmwareTrackAssignmentDetails ret = new FirmwareTrackAssignmentDetails();
        int colIdx=1;
        colIdx++;

        //add columns from properties FirmwareVersion in here. 
        //make sure order of fields is the same as defined in FirmwareVersion
        ret.setEquipmentType(EquipmentType.getById(rs.getInt(colIdx++)));
        ret.setModelId(rs.getString(colIdx++));
        ret.setVersionName(rs.getString(colIdx++));
        ret.setCommit(rs.getString(colIdx++));
        ret.setDescription(rs.getString(colIdx++));
        
        // Skip filename, validation method and validation code
        colIdx++;
        colIdx++;
        colIdx++;

        ret.setReleaseDate(rs.getLong(colIdx++));
        
        // Skip created and modified on the firmware version.
        colIdx++;
        colIdx++;
        
        ret.setTrackRecordId(rs.getLong(colIdx++));
        ret.setFirmwareVersionRecordId(rs.getLong(colIdx++));
        ret.setDefaultRevisionForTrack(rs.getBoolean(colIdx++));
        ret.setDeprecated(rs.getBoolean(colIdx++));
        ret.setCreatedTimestamp(rs.getLong(colIdx++));
        ret.setLastModifiedTimestamp(rs.getLong(colIdx++));
        

        return ret;
    }

}
