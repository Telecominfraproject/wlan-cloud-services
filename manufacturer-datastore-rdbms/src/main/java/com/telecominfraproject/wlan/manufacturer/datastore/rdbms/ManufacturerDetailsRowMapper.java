package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;

/**
 * @author mpreston
 *
 */

public class ManufacturerDetailsRowMapper implements RowMapper<ManufacturerDetailsRecord> {
    
    public ManufacturerDetailsRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        ManufacturerDetailsRecord manufacturerDetails = new ManufacturerDetailsRecord();
        int colIdx=1;
        manufacturerDetails.setId(rs.getLong(colIdx++));

        //add columns from properties ManufacturerDetailsRecord in here. 
        //make sure order of fields is the same as defined in ManufacturerDetailsRecord
 
        manufacturerDetails.setManufacturerName(rs.getString(colIdx++));
        manufacturerDetails.setManufacturerAlias(rs.getString(colIdx++));
        
        manufacturerDetails.setCreatedTimestamp(rs.getLong(colIdx++));
        manufacturerDetails.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return manufacturerDetails;
    }
}
