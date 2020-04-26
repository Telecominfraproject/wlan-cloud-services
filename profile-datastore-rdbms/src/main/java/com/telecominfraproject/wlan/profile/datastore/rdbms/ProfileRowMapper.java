package com.telecominfraproject.wlan.profile.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;

/**
 * @author dtoptygin
 *
 */

public class ProfileRowMapper implements RowMapper<Profile> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProfileRowMapper.class);

    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        int colIdx=1;
        profile.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Profile in here. 
        //make sure order of fields is the same as defined in Profile
        profile.setCustomerId(rs.getInt(colIdx++));
        profile.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	ProfileDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ProfileDetails.class);
                profile.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ProfileDetails from database for id = {}", profile.getId());
            }
        }

        profile.setCreatedTimestamp(rs.getLong(colIdx++));
        profile.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return profile;
    }
}