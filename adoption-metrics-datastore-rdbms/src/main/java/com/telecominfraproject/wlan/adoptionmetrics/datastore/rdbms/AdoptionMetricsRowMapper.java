package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;

/**
 * @author dtoptygin
 *
 */

public class AdoptionMetricsRowMapper implements RowMapper<ServiceAdoptionMetrics> {
    
    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsRowMapper.class);

    public ServiceAdoptionMetrics mapRow(ResultSet rs, int rowNum) throws SQLException {
        ServiceAdoptionMetrics serviceAdoptionMetrics = new ServiceAdoptionMetrics();
        int colIdx=1;
        serviceAdoptionMetrics.setYear(rs.getInt(colIdx++));
        serviceAdoptionMetrics.setMonth(rs.getInt(colIdx++));
        serviceAdoptionMetrics.setWeekOfYear(rs.getInt(colIdx++));
        serviceAdoptionMetrics.setDayOfYear(rs.getInt(colIdx++));

        //TODO: add columns from properties ServiceAdoptionMetrics in here. 
        //make sure order of fields is the same as defined in ServiceAdoptionMetrics
        serviceAdoptionMetrics.setCustomerId(rs.getInt(colIdx++));
        serviceAdoptionMetrics.setLocationId(rs.getLong(colIdx++));
        serviceAdoptionMetrics.setEquipmentId(rs.getLong(colIdx++));
        
        serviceAdoptionMetrics.setNumUniqueConnectedMacs(rs.getLong(colIdx++));
        serviceAdoptionMetrics.setNumBytesUpstream(rs.getLong(colIdx++));
        serviceAdoptionMetrics.setNumBytesDownstream(rs.getLong(colIdx++));
        
        return serviceAdoptionMetrics;
    }
}