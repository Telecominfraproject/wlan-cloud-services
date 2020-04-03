package com.telecominfraproject.wlan.location.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationType;

/**
 * @author dtoptygin
 *
 */

public class LocationRowMapper implements RowMapper<Location> {
    
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();
        int colIdx=1;
        location.setId(rs.getInt(colIdx++));

        //add columns from properties Location in here. 
        //make sure order of fields is the same as defined in Location
        location.setLocationType(LocationType.getById(rs.getInt(colIdx++)));
        location.setCustomerId(rs.getInt(colIdx++));
        location.setName(rs.getString(colIdx++));
        location.setParentId(rs.getLong(colIdx++));
        location.setDetails(LocationDAO.generateDetails(rs.getString(colIdx++)));
        location.setCreatedTimestamp(rs.getLong(colIdx++));
        location.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return location;
    }
}
