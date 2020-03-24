package com.telecominfraproject.wlan.servicemetrics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

/**
 * A single metric record contains normalized fields
 * 
 * @author yongli
 * 
 */
public class SingleMetricRecord extends MetricRecord  implements HasCustomerId, HasEquipmentId, HasProducedTimestamp {
    private static final long serialVersionUID = 6213696913539984507L;

    private int customerId;

    private long equipmentId;

    public SingleMetricRecord() {
        
    }
    
    public SingleMetricRecord(int customerId, long equipmentId) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public SingleMetricRecord clone() {
        SingleMetricRecord result = (SingleMetricRecord) super.clone();
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + customerId;
        result = prime * result + (int) (equipmentId ^ (equipmentId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SingleMetricRecord)) {
            return false;
        }
        SingleMetricRecord other = (SingleMetricRecord) obj;
        if (customerId != other.customerId) {
            return false;
        }
        if (equipmentId != other.equipmentId) {
            return false;
        }
        return true;
    }

    @Override
    @JsonIgnore
    public long getProducedTimestampMs() {
        return getCreatedTimestamp();
    }

    @JsonIgnore
    public static SingleMetricRecord createDeviceMetrics(int customerId, long equipmentId, Long startTime, MetricData metrics) 
    {
        SingleMetricRecord metricRecord = new SingleMetricRecord();
        metricRecord.setCustomerId(customerId);
        metricRecord.setEquipmentId(equipmentId);
        if (null != startTime) {
            metricRecord.setCreatedTimestamp(startTime);
        }
        metricRecord.setData(metrics);
        return metricRecord;
    }

    
    
}
