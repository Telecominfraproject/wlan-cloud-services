package com.telecominfraproject.wlan.status.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author dtoptygin
 *
 */

public class StatusRowMapper implements RowMapper<Status> {
    
    private static final Logger LOG = LoggerFactory.getLogger(StatusRowMapper.class);

    public Status mapRow(ResultSet rs, int rowNum) throws SQLException {
        Status status = new Status();
        int colIdx=1;
        status.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties Status in here. 
        //make sure order of fields is the same as defined in Status
        status.setCustomerId(rs.getInt(colIdx++));
        status.setEquipmentId(rs.getLong(colIdx++));
        status.setStatusDataType(StatusDataType.getById(rs.getInt(colIdx++)));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	StatusDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, StatusDetails.class);
                status.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode StatusDetails from database for id = {}", status.getId());
            }
        }

        status.setCreatedTimestamp(rs.getLong(colIdx++));
        status.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return status;
    }
}