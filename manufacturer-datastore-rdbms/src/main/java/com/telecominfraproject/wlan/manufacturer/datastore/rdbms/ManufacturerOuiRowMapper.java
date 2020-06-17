package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */

public class ManufacturerOuiRowMapper implements RowMapper<ManufacturerOuiDetails> {
    
    public ManufacturerOuiDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        ManufacturerOuiDetails clientOui = new ManufacturerOuiDetails();
        int colIdx=1;
        clientOui.setOui(rs.getString(colIdx++));
        clientOui.setManufacturerName(rs.getString(colIdx++));
        clientOui.setManufacturerAlias(rs.getString(colIdx++));
        
        return clientOui;
    }
}
