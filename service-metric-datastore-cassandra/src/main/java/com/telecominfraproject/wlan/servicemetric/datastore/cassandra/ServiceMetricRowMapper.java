package com.telecominfraproject.wlan.servicemetric.datastore.cassandra;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.cql.Row;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

/**
 * @author dtoptygin
 *
 */

public class ServiceMetricRowMapper implements RowMapper<ServiceMetric> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricRowMapper.class);

    public ServiceMetric mapRow(Row row)  {
    	ServiceMetric serviceMetric = new ServiceMetric();

        serviceMetric.setCustomerId(row.getInt("customerId"));
        serviceMetric.setEquipmentId(row.getLong("equipmentId"));
        serviceMetric.setClientMac(row.getLong("clientMac"));

        serviceMetric.setDataType(ServiceMetricDataType.getById(row.getInt("dataType")));
        
        ByteBuffer byteBuffer = row.getByteBuffer("details");
        byte[] zippedBytes = byteBuffer.hasArray() ? byteBuffer.array() : null;
        
        if (zippedBytes !=null) {
            try {
            	ServiceMetricDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, ServiceMetricDetails.class);
                serviceMetric.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode ServiceMetricDetails from database for {}:{}:{}", serviceMetric.getCustomerId(), serviceMetric.getEquipmentId(), serviceMetric.getDataType());
            }
        }

        serviceMetric.setCreatedTimestamp(row.getLong("createdTimestamp"));
        
        return serviceMetric;
    	
    }
}
