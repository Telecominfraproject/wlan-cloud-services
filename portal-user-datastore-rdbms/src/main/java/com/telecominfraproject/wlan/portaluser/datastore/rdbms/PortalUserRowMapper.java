package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.portaluser.models.PortalUser;
import com.telecominfraproject.wlan.portaluser.models.PortalUserDetails;

/**
 * @author dtoptygin
 *
 */

public class PortalUserRowMapper implements RowMapper<PortalUser> {
    
    private static final Logger LOG = LoggerFactory.getLogger(PortalUserRowMapper.class);

    public PortalUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortalUser portalUser = new PortalUser();
        int colIdx=1;
        portalUser.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties PortalUser in here. 
        //make sure order of fields is the same as defined in PortalUser
        portalUser.setCustomerId(rs.getInt(colIdx++));
        portalUser.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	PortalUserDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, PortalUserDetails.class);
                portalUser.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode PortalUserDetails from database for id = {}", portalUser.getId());
            }
        }

        portalUser.setCreatedTimestamp(rs.getLong(colIdx++));
        portalUser.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return portalUser;
    }
}