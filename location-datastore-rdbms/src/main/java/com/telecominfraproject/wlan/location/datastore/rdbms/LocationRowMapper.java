package com.telecominfraproject.wlan.location.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationType;

/**
 * @author dtoptygin
 *
 */

public class LocationRowMapper implements RowMapper<Location> {

    private static final Logger LOG = LoggerFactory.getLogger(LocationRowMapper.class);

    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();
        int colIdx=1;
        location.setId(rs.getInt(colIdx++));

        //add columns from properties Location in here. 
        //make sure order of fields is the same as defined in Location
        location.setLocationType(LocationType.getById(rs.getInt(colIdx++)));
        location.setCustomerId(rs.getInt(colIdx++));
        location.setName(rs.getString(colIdx++));
        location.setParentId(rs.getLong(colIdx++)); //when DB value is null, parentId is set to 0
        colIdx++; // skip getting LocationDetails from 'details' column. Instead use detailsBin

        location.setCreatedTimestamp(rs.getLong(colIdx++));
        location.setLastModifiedTimestamp(rs.getLong(colIdx++));

        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
                LocationDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, LocationDetails.class);
                location.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode LocationDetails from database for id = {}", location.getId());
            }
        }

        return location;
    }
}
