package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

/**
 * @author dtoptygin
 *
 */

public class ServiceMetricRowMapper implements RowMapper<ServiceMetric> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricRowMapper.class);

    public ServiceMetric mapRow(ResultSet rs, int rowNum) throws SQLException {
        ServiceMetric serviceMetric = new ServiceMetric();
        int colIdx=1;
        serviceMetric.setId(rs.getLong(colIdx++));

        //TODO: add columns from properties ServiceMetric in here. 
        //make sure order of fields is the same as defined in ServiceMetric
        serviceMetric.setCustomerId(rs.getInt(colIdx++));
        serviceMetric.setSampleStr(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	ServiceMetricDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ServiceMetricDetails.class);
                serviceMetric.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ServiceMetricDetails from database for id = {}", serviceMetric.getId());
            }
        }

        serviceMetric.setCreatedTimestamp(rs.getLong(colIdx++));
        serviceMetric.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return serviceMetric;
    }
}