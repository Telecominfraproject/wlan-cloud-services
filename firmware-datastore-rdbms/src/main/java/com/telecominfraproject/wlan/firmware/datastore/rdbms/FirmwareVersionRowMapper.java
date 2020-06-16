package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.firmware.models.ValidationMethod;


/**
 * @author ekeddy
 *
 */

public class FirmwareVersionRowMapper implements RowMapper<FirmwareVersion> {
    
    public FirmwareVersion mapRow(ResultSet rs, int rowNum) throws SQLException {
        FirmwareVersion firmwareVersion = new FirmwareVersion();
        int colIdx=1;
        firmwareVersion.setId(rs.getLong(colIdx++));

        //add columns from properties FirmwareVersion in here. 
        //make sure order of fields is the same as defined in FirmwareVersion
        firmwareVersion.setEquipmentType(EquipmentType.getById(rs.getInt(colIdx++)));
        firmwareVersion.setModelId(rs.getString(colIdx++));
        firmwareVersion.setVersionName(rs.getString(colIdx++));
        firmwareVersion.setCommit(rs.getString(colIdx++));
        firmwareVersion.setDescription(rs.getString(colIdx++));
        firmwareVersion.setFilename(rs.getString(colIdx++));
        firmwareVersion.setValidationMethod(ValidationMethod.getById(rs.getInt(colIdx++)));
        firmwareVersion.setValidationCode(rs.getString(colIdx++));
        firmwareVersion.setReleaseDate(rs.getLong(colIdx++));
           
        firmwareVersion.setCreatedTimestamp(rs.getLong(colIdx++));
        firmwareVersion.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        
        return firmwareVersion;
    }
}
