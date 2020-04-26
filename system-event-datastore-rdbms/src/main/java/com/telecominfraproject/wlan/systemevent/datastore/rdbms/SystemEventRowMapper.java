package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecordDetails;

/**
 * @author dtoptygin
 *
 */

public class SystemEventRowMapper implements RowMapper<SystemEventContainer> {
    
    private static final Logger LOG = LoggerFactory.getLogger(SystemEventRowMapper.class);

    public SystemEventContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
        SystemEventContainer systemEventRecord = new SystemEventContainer();
        int colIdx=1;
        systemEventRecord.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties SystemEventContainer in here. 
        //make sure order of fields is the same as defined in SystemEventContainer
        systemEventRecord.setCustomerId(rs.getInt(colIdx++));
        systemEventRecord.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	SystemEventRecordDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, SystemEventRecordDetails.class);
                systemEventRecord.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode SystemEventRecordDetails from database for id = {}", systemEventRecord.getId());
            }
        }

        systemEventRecord.setCreatedTimestamp(rs.getLong(colIdx++));
        systemEventRecord.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return systemEventRecord;
    }
}