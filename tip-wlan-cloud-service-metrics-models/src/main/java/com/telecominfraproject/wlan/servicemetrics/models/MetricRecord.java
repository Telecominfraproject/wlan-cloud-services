package com.telecominfraproject.wlan.servicemetrics.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class MetricRecord extends BaseJsonModel {

    private static final long serialVersionUID = -1875577097923070125L;

    private long id;
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    private MetricData data;
    private long processingStartTime;
    private String deploymentId;

    public MetricRecord(long id, long createdTimestamp, long lastModifiedTimestamp, MetricData data) {
        this.id = id;
        this.createdTimestamp = createdTimestamp;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.data = data;
    }

    public MetricRecord() {
    }
    
    /**
     * Return the record type.
     * 
     * @return
     */
    public String getDataType() {
        if (null != this.data) {
            return this.data.getType();
        }
        return null;
    }

    /**
     * Return the data as JSON document
     * 
     * @return
     */
    public String toJSONData() {
        if (null != this.data) {
            return data.toString();
        }
        return null;
    }
    
    /**
     * Return the data as compressed JSON document
     * @return
     */
    public byte[] toZippedJSONData() {
        if (null != this.data) {
            return data.toZippedBytes();
        }
        return null;
    }

    /**
     * Set data from JSON document
     * 
     * @return
     */
    public void fromJSONData(String jsonDoc) {
        this.data = (MetricData) MetricData.fromString(jsonDoc,
                MetricData.class);
    }
    
    /**
     * Set the data from compressed JSON document
     * @param compressedJson
     */
    public void fromZippedJSONData(byte[] compressedJson) {
        this.data = (MetricData) MetricData.fromZippedBytes(compressedJson, MetricData.class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public MetricData getData() {
        return this.data;
    }
    
    public void setData(MetricData data) {
        this.data = data;
    }

	public long getProcessingStartTime() {
		return processingStartTime;
	}

	public void setProcessingStartTime(long processingStartTime) {
		this.processingStartTime = processingStartTime;
	}

    @Override
    public MetricRecord clone() {
        MetricRecord ret = (MetricRecord) super.clone();
        if (null != this.data) {
            ret.data = (MetricData) this.data.clone();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (createdTimestamp ^ (createdTimestamp >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MetricRecord)) {
            return false;
        }
        MetricRecord other = (MetricRecord) obj;
        if (createdTimestamp != other.createdTimestamp) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
