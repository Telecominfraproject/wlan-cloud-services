package com.telecominfraproject.wlan.routing.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.routing.models.Routing;
import com.telecominfraproject.wlan.routing.models.RoutingDetails;

/**
 * @author dtoptygin
 *
 */

public class RoutingRowMapper implements RowMapper<Routing> {
    
    private static final Logger LOG = LoggerFactory.getLogger(RoutingRowMapper.class);

    public Routing mapRow(ResultSet rs, int rowNum) throws SQLException {
        Routing routing = new Routing();
        int colIdx=1;
        routing.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Routing in here. 
        //make sure order of fields is the same as defined in Routing
        routing.setCustomerId(rs.getInt(colIdx++));
        routing.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	RoutingDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, RoutingDetails.class);
                routing.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode RoutingDetails from database for id = {}", routing.getId());
            }
        }

        routing.setCreatedTimestamp(rs.getLong(colIdx++));
        routing.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return routing;
    }
}