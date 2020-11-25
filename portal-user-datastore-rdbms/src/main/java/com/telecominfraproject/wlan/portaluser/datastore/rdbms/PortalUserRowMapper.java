package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;
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
        portalUser.setUsername(rs.getString(colIdx++));
        portalUser.setPassword(rs.getString(colIdx++));
        portalUser.setRole(PortalUserRole.getById(rs.getInt(colIdx++)));
        
        String rolesString = rs.getString(colIdx++);
        rolesString = rolesString.replace("[", ""); // remove brackets
        rolesString = rolesString.replace("]", ""); 
        rolesString = rolesString.replace(" ", ""); 
        List<String> listOfRoleIds = Arrays.asList(rolesString.split(",", -1));
        List<PortalUserRole> listOfRoles = new ArrayList<>();
        listOfRoleIds.forEach(y -> listOfRoles.add(PortalUserRole.getById(Integer.parseInt(y))));
        portalUser.setRoles(listOfRoles);
        
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