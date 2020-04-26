package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetails;

/**
 * @author dtoptygin
 *
 */

public class ManufacturerRowMapper implements RowMapper<Manufacturer> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerRowMapper.class);

    public Manufacturer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        int colIdx=1;
        manufacturer.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Manufacturer in here. 
        //make sure order of fields is the same as defined in Manufacturer
        manufacturer.setCustomerId(rs.getInt(colIdx++));
        manufacturer.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	ManufacturerDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ManufacturerDetails.class);
                manufacturer.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ManufacturerDetails from database for id = {}", manufacturer.getId());
            }
        }

        manufacturer.setCreatedTimestamp(rs.getLong(colIdx++));
        manufacturer.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return manufacturer;
    }
}